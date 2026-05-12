package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.MaterialKind;
import com.troblecodings.tcutility.utils.MaterialKindRegistry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
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
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.TRUE));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING,
                context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(final BlockState state,
            final net.minecraft.world.level.Level world, final BlockPos pos, final Player player,
            final InteractionHand hand, final BlockHitResult hit) {
        if (MaterialKindRegistry.get(state.getBlock()) == MaterialKind.METAL) {
            return InteractionResult.PASS;
        }
        toggleAt(world, pos, state);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void neighborChanged(final BlockState state, final net.minecraft.world.level.Level world,
            final BlockPos pos, final Block fromBlock, final BlockPos fromPos,
            final boolean isMoving) {
        if (world.isClientSide) {
            return;
        }
        final boolean powered = world.hasNeighborSignal(pos);
        if (powered != state.getValue(OPEN)) {
            // Redstone: powered -> open. Closed initial state handled separately.
            final BlockState newState = state.setValue(OPEN, powered);
            world.setBlock(pos, newState, 2);
            applyRoll(world, pos, newState);
            propagateAlongAxis(world, pos, newState);
            world.levelEvent(null, soundFor(state, powered), pos, 0);
        }
    }

    @Override
    public BlockState playerWillDestroy(final net.minecraft.world.level.Level world, final BlockPos pos,
            final BlockState state, final Player player) {
        // Beim Brechen des Headers: alle daran haengenden Gates entfernen
        // (sonst bleibt ein freischwebendes Tor zurueck).
        clearGatesBelow(world, pos);
        return super.playerWillDestroy(world, pos, state, player);
    }

    /**
     * Toggle des Headers an {@code pos}: flippt OPEN, rollt die zugehoerigen
     * Gates entsprechend, propagiert den State zu Nachbar-Headern. Public,
     * damit auch ein {@link TCGarageGate}-Klick (von unten) das ganze Tor
     * ausloesen kann -- so wie es das 1.12.2-Original macht.
     */
    public void toggleAt(final net.minecraft.world.level.Level world, final BlockPos pos,
            final BlockState state) {
        final BlockState toggled = state.cycle(OPEN);
        world.setBlock(pos, toggled, 10);
        applyRoll(world, pos, toggled);
        propagateAlongAxis(world, pos, toggled);
        world.levelEvent(null, soundFor(state, toggled.getValue(OPEN)), pos, 0);
    }

    /**
     * Setzt den State des angegebenen Headers + rollt seine Gates auf bzw.
     * zu. Wird auch fuer Nachbar-Header verwendet.
     */
    private void applyRoll(final net.minecraft.world.level.Level world, final BlockPos pos,
            final BlockState state) {
        if (state.getValue(OPEN)) {
            // Rolltor faehrt hoch -> Gates entfernen
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos below = pos.below(i);
                if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                    break;
                }
                world.setBlock(below, Blocks.AIR.defaultBlockState(), 11);
            }
        } else {
            // Rolltor faehrt runter -> Gates platzieren bis ein non-air block
            // den Weg blockiert
            final Block gate = resolveGateBlock();
            if (gate == null) {
                return;
            }
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos below = pos.below(i);
                final BlockState belowState = world.getBlockState(below);
                if (!belowState.isAir()) {
                    break;
                }
                world.setBlock(below, gate.defaultBlockState()
                        .setValue(TCCubeRotation.FACING, state.getValue(FACING)), 11);
            }
        }
    }

    /**
     * Synchronisiert den OPEN-State mit gleichgerichteten Nachbar-Headern
     * links und rechts, damit ein breites Garagentor als ein Stueck wirkt.
     * Achse fuer "links/rechts" haengt von {@code FACING} ab.
     */
    private void propagateAlongAxis(final net.minecraft.world.level.Level world, final BlockPos pos,
            final BlockState state) {
        final Direction facing = state.getValue(FACING);
        final Direction[] sides;
        if (facing.getAxis() == Direction.Axis.Z) {
            sides = new Direction[] { Direction.EAST, Direction.WEST };
        } else {
            sides = new Direction[] { Direction.NORTH, Direction.SOUTH };
        }
        for (final Direction d : sides) {
            for (int i = 1; i < MAX_REACH; i++) {
                final BlockPos p = pos.relative(d, i);
                final BlockState s = world.getBlockState(p);
                if (!(s.getBlock() instanceof TCGarageDoor)) {
                    break;
                }
                if (s.getValue(OPEN) == state.getValue(OPEN)) {
                    break; // schon synchron
                }
                final BlockState updated = s.setValue(OPEN, state.getValue(OPEN));
                world.setBlock(p, updated, 10);
                applyRoll(world, p, updated);
            }
        }
    }

    private void clearGatesBelow(final net.minecraft.world.level.Level world, final BlockPos pos) {
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos below = pos.below(i);
            if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                break;
            }
            world.setBlock(below, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    @Nullable
    private Block resolveGateBlock() {
        final ResourceLocation rl = this.builtInRegistryHolder().key().location();
        if (rl == null) {
            return null;
        }
        return ForgeRegistries.BLOCKS
                .getValue(new ResourceLocation(rl.getNamespace(), rl.getPath() + "_gate"));
    }

    private static int soundFor(final BlockState state, final boolean opening) {
        // 1005/1006 = open wood/iron, 1011/1012 = close wood/iron in 1.14.4
        if (MaterialKindRegistry.get(state.getBlock()) == MaterialKind.METAL) {
            return opening ? 1005 : 1011;
        }
        return opening ? 1006 : 1012;
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }
}
