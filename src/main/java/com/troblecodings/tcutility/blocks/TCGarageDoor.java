package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Rolltor-Header. Wenn man den Block aktiviert, rollt das Tor unter ihm
 * auf oder zu: bei OPEN werden alle TCGarageGates darunter (bis 10 Bloecke)
 * zu Luft, bei CLOSED werden sie aus dem zugehoerigen Gate-Block-Type
 * wiederhergestellt. Plus: hat der Header einen Nachbar-Header in der
 * gleichen Achse (FACING-orthogonal), wird der State auch dort
 * propagiert -- so funktionieren breitere Tore mit mehreren Saeulen.
 *
 * Der zugehoerige Gate-Block wird ueber Registry-Naming aufgeloest:
 * {@code <namespace>:<basename>_gate}, identisch zur 1.12.2-Logik.
 */
public class TCGarageDoor extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private static final int MAX_REACH = 10;

    public TCGarageDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH).with(OPEN, Boolean.TRUE));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.getDefaultState().with(FACING,
                context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public ActionResultType onBlockActivated(final BlockState state,
            final net.minecraft.world.World world, final BlockPos pos, final PlayerEntity player,
            final Hand hand, final BlockRayTraceResult hit) {
        if (state.getMaterial() == Material.IRON) {
            return ActionResultType.PASS;
        }
        toggleAt(world, pos, state);
        return ActionResultType.func_233537_a_(world.isRemote);
    }

    @Override
    public void neighborChanged(final BlockState state, final net.minecraft.world.World world,
            final BlockPos pos, final Block fromBlock, final BlockPos fromPos,
            final boolean isMoving) {
        if (world.isRemote) {
            return;
        }
        final boolean powered = world.isBlockPowered(pos);
        if (powered != state.get(OPEN)) {
            // Redstone: powered -> open. Closed initial state handled separately.
            final BlockState newState = state.with(OPEN, powered);
            world.setBlockState(pos, newState, 2);
            applyRoll(world, pos, newState);
            propagateAlongAxis(world, pos, newState);
            world.playEvent(null, soundFor(state, powered), pos, 0);
        }
    }

    @Override
    public void onBlockHarvested(final net.minecraft.world.World world, final BlockPos pos,
            final BlockState state, final PlayerEntity player) {
        // Beim Brechen des Headers: alle daran haengenden Gates entfernen
        // (sonst bleibt ein freischwebendes Tor zurueck).
        clearGatesBelow(world, pos);
        super.onBlockHarvested(world, pos, state, player);
    }

    /**
     * Toggle des Headers an {@code pos}: flippt OPEN, rollt die zugehoerigen
     * Gates entsprechend, propagiert den State zu Nachbar-Headern. Public,
     * damit auch ein {@link TCGarageGate}-Klick (von unten) das ganze Tor
     * ausloesen kann -- so wie es das 1.12.2-Original macht.
     */
    public void toggleAt(final net.minecraft.world.World world, final BlockPos pos,
            final BlockState state) {
        final BlockState toggled = state.cycleValue(OPEN);
        world.setBlockState(pos, toggled, 10);
        applyRoll(world, pos, toggled);
        propagateAlongAxis(world, pos, toggled);
        world.playEvent(null, soundFor(state, toggled.get(OPEN)), pos, 0);
    }

    /**
     * Setzt den State des angegebenen Headers + rollt seine Gates auf bzw.
     * zu. Wird auch fuer Nachbar-Header verwendet.
     */
    private void applyRoll(final net.minecraft.world.World world, final BlockPos pos,
            final BlockState state) {
        if (state.get(OPEN)) {
            // Rolltor faehrt hoch -> Gates entfernen
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos below = pos.down(i);
                if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                    break;
                }
                world.setBlockState(below, Blocks.AIR.getDefaultState(), 11);
            }
        } else {
            // Rolltor faehrt runter -> Gates platzieren bis ein non-air block
            // den Weg blockiert
            final Block gate = resolveGateBlock();
            if (gate == null) {
                return;
            }
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos below = pos.down(i);
                final BlockState belowState = world.getBlockState(below);
                if (!belowState.isAir()) {
                    break;
                }
                world.setBlockState(below, gate.getDefaultState()
                        .with(TCCubeRotation.FACING, state.get(FACING)), 11);
            }
        }
    }

    /**
     * Synchronisiert den OPEN-State mit gleichgerichteten Nachbar-Headern
     * links und rechts, damit ein breites Garagentor als ein Stueck wirkt.
     * Achse fuer "links/rechts" haengt von {@code FACING} ab.
     */
    private void propagateAlongAxis(final net.minecraft.world.World world, final BlockPos pos,
            final BlockState state) {
        final Direction facing = state.get(FACING);
        final Direction[] sides;
        if (facing.getAxis() == Direction.Axis.Z) {
            sides = new Direction[] { Direction.EAST, Direction.WEST };
        } else {
            sides = new Direction[] { Direction.NORTH, Direction.SOUTH };
        }
        for (final Direction d : sides) {
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos p = pos.offset(d, i);
                final BlockState s = world.getBlockState(p);
                if (!(s.getBlock() instanceof TCGarageDoor)) {
                    break;
                }
                if (s.get(OPEN) == state.get(OPEN)) {
                    break; // schon synchron
                }
                final BlockState updated = s.with(OPEN, state.get(OPEN));
                world.setBlockState(p, updated, 10);
                applyRoll(world, p, updated);
            }
        }
    }

    private void clearGatesBelow(final net.minecraft.world.World world, final BlockPos pos) {
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos below = pos.down(i);
            if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                break;
            }
            world.setBlockState(below, Blocks.AIR.getDefaultState(), 35);
        }
    }

    @Nullable
    private Block resolveGateBlock() {
        final ResourceLocation rl = this.getRegistryName();
        if (rl == null) {
            return null;
        }
        return ForgeRegistries.BLOCKS
                .getValue(new ResourceLocation(rl.getNamespace(), rl.getPath() + "_gate"));
    }

    private static int soundFor(final BlockState state, final boolean opening) {
        // 1005/1006 = open wood/iron, 1011/1012 = close wood/iron in 1.14.4
        if (state.getMaterial() == Material.IRON) {
            return opening ? 1005 : 1011;
        }
        return opening ? 1006 : 1012;
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }
}
