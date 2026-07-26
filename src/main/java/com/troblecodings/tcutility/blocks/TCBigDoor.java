package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Drei-Block-tall Door-Variante. Im Gegensatz zum 1.14-Vanilla-DoorBlock
 * (zwei Blocks: LOWER/UPPER) trackt {@link #THIRD} drei Stockwerke
 * (LOWER/MIDDLE/UPPER); Open- und Power-State liegen am untersten Block,
 * die anderen zwei spiegeln den State.
 */
public class TCBigDoor extends Block {

    public enum BigDoorThird implements IStringSerializable {
        LOWER("lower"), MIDDLE("middle"), UPPER("upper");

        private final String name;

        BigDoorThird(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<BigDoorThird> THIRD = EnumProperty.create("third",
            BigDoorThird.class);

    // 1.12-AABB-Tabelle 1:1 portiert: Tuer ist 1,5 Bloecke breit, ragt also
    // um 0,5 Bloecke in den Nachbarblock. Das ist absichtlich -- die alte
    // Hitbox blockt damit Right-Click-Placement im "Schwung-Block" der Tuer,
    // sodass zwei Doppeltueren nicht beide LEFT-hinged ins gleiche Volumen
    // gesetzt werden koennen. Werte in 0..16-Skala.
    private static final VoxelShape SOUTH_L_AABB = Block.makeCuboidShape(0, 0, 0, 24, 16, 3);
    private static final VoxelShape NORTH_L_AABB = Block.makeCuboidShape(0, 0, 13, 24, 16, 16);
    private static final VoxelShape WEST_L_AABB = Block.makeCuboidShape(13, 0, 0, 16, 16, 24);
    private static final VoxelShape EAST_L_AABB = Block.makeCuboidShape(0, 0, 0, 3, 16, 24);
    private static final VoxelShape SOUTH_R_AABB = Block.makeCuboidShape(-8, 0, 0, 16, 16, 3);
    private static final VoxelShape NORTH_R_AABB = Block.makeCuboidShape(-8, 0, 13, 16, 16, 16);
    private static final VoxelShape WEST_R_AABB = Block.makeCuboidShape(13, 0, -8, 16, 16, 16);
    private static final VoxelShape EAST_R_AABB = Block.makeCuboidShape(0, 0, -8, 3, 16, 16);

    private Item item;

    public TCBigDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, Boolean.FALSE)
                .with(HINGE, DoorHingeSide.LEFT)
                .with(POWERED, Boolean.FALSE)
                .with(THIRD, BigDoorThird.LOWER));
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world,
            final BlockPos pos, final ISelectionContext context) {
        // 1:1-Port aus 1.12 BlockBigDoor.getBoundingBox: 4 Facings * 2 Hinges
        // * 2 Open-States = 16 Kombinationen, alle auf 8 unterschiedliche
        // 1,5-Block-AABBs gemappt. closed=!OPEN, rightHinge=HINGE==RIGHT.
        final boolean closed = !state.get(OPEN);
        final boolean rightHinge = state.get(HINGE) == DoorHingeSide.RIGHT;
        switch (state.get(FACING)) {
            case EAST:
            default:
                if (!closed && !rightHinge) return SOUTH_L_AABB;
                else if (!closed && rightHinge) return NORTH_L_AABB;
                else if (closed && !rightHinge) return EAST_L_AABB;
                else return EAST_R_AABB;
            case SOUTH:
                if (!closed && !rightHinge) return WEST_L_AABB;
                else if (!closed && rightHinge) return EAST_L_AABB;
                else if (closed && !rightHinge) return SOUTH_R_AABB;
                else return SOUTH_L_AABB;
            case WEST:
                if (!closed && !rightHinge) return NORTH_R_AABB;
                else if (!closed && rightHinge) return SOUTH_R_AABB;
                else if (closed && !rightHinge) return WEST_R_AABB;
                else return WEST_L_AABB;
            case NORTH:
                if (!closed && !rightHinge) return EAST_R_AABB;
                else if (!closed && rightHinge) return WEST_R_AABB;
                else if (closed && !rightHinge) return NORTH_L_AABB;
                else return NORTH_R_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos,
            final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (state.getMaterial() == Material.IRON) {
            return false;
        }
        final BlockPos lowerPos = lowerPosOf(state, pos);
        final BlockState lowerState = world.getBlockState(lowerPos);
        if (!(lowerState.getBlock() instanceof TCBigDoor)) {
            return false;
        }
        final boolean newOpen = !lowerState.get(OPEN);
        propagateState(world, lowerPos, lowerState, OPEN, newOpen);
        world.playEvent(player, newOpen ? openSoundEvent(state) : closeSoundEvent(state),
                pos, 0);
        return true;
    }

    @Override
    public void neighborChanged(final BlockState state, final World world, final BlockPos pos,
            final Block fromBlock, final BlockPos fromPos, final boolean isMoving) {
        // 1:1-Port aus 1.12 BlockDoor.neighborChanged: UPPER und MIDDLE
        // pruefen Struktur-Integritaet und delegieren Redstone an LOWER;
        // LOWER pruefst Untergrund + alle drei Glieder, dropped beim
        // Wegbrechen, und schaltet Powered/Open synchron auf Redstone.
        switch (state.get(THIRD)) {
            case UPPER: {
                final BlockPos middle = pos.down();
                final BlockPos lower = pos.down(2);
                final BlockState middleState = world.getBlockState(middle);
                final BlockState lowerState = world.getBlockState(lower);
                if (!(middleState.getBlock() instanceof TCBigDoor)
                        || !(lowerState.getBlock() instanceof TCBigDoor)) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
                } else if (!(fromBlock instanceof TCBigDoor)) {
                    middleState.neighborChanged(world, middle, fromBlock, fromPos, isMoving);
                }
                return;
            }
            case MIDDLE: {
                final BlockPos lower = pos.down();
                final BlockPos upper = pos.up();
                final BlockState lowerState = world.getBlockState(lower);
                final BlockState upperState = world.getBlockState(upper);
                if (!(lowerState.getBlock() instanceof TCBigDoor)
                        || !(upperState.getBlock() instanceof TCBigDoor)) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
                } else if (!(fromBlock instanceof TCBigDoor)) {
                    lowerState.neighborChanged(world, lower, fromBlock, fromPos, isMoving);
                }
                return;
            }
            case LOWER:
            default:
                break;
        }
        // LOWER-Pfad: Struktur und Redstone.
        final BlockPos middle = pos.up();
        final BlockPos upper = pos.up(2);
        final BlockState middleState = world.getBlockState(middle);
        final BlockState upperState = world.getBlockState(upper);
        boolean broken = false;
        if (!(middleState.getBlock() instanceof TCBigDoor)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
            broken = true;
        }
        if (!(upperState.getBlock() instanceof TCBigDoor)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
            broken = true;
        }
        if (!world.getBlockState(pos.down()).getMaterial().isSolid()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
            broken = true;
            if (middleState.getBlock() instanceof TCBigDoor) {
                world.setBlockState(middle, Blocks.AIR.getDefaultState(), 35);
            }
            if (upperState.getBlock() instanceof TCBigDoor) {
                world.setBlockState(upper, Blocks.AIR.getDefaultState(), 35);
            }
        }
        if (broken) {
            if (!world.isRemote && this.item != null) {
                Block.spawnAsEntity(world, pos, new ItemStack(this.item));
            }
            return;
        }
        final boolean powered = world.isBlockPowered(pos) || world.isBlockPowered(middle)
                || world.isBlockPowered(upper);
        if (!(fromBlock instanceof TCBigDoor)
                && (powered || fromBlock.getDefaultState().canProvidePower())
                && powered != state.get(POWERED)) {
            BlockState s = state.with(POWERED, powered);
            if (powered != state.get(OPEN)) {
                s = s.with(OPEN, powered);
                world.playEvent(null, powered ? openSoundEvent(s) : closeSoundEvent(s), pos, 0);
            }
            propagateState(world, pos, s, POWERED, powered);
            propagateState(world, pos, s, OPEN, s.get(OPEN));
        }
    }

    @Override
    public void onBlockHarvested(final World world, final BlockPos pos, final BlockState state,
            final PlayerEntity player) {
        if (player.abilities.isCreativeMode) {
            removeAllParts(world, pos, state);
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void onReplaced(final BlockState state, final World world, final BlockPos pos,
            final BlockState newState, final boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            // Wenn der Block via Explosion / setBlockState aus der Welt
            // verschwindet (nicht via onBlockHarvested), trotzdem die
            // anderen zwei Glieder nachreissen.
            removeAllParts(world, pos, state);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    /**
     * Position des LOWER-Blocks unseres 3er-Clusters relativ zu {@code pos}.
     */
    public static BlockPos lowerPosOf(final BlockState state, final BlockPos pos) {
        switch (state.get(THIRD)) {
            case UPPER:
                return pos.down(2);
            case MIDDLE:
                return pos.down();
            case LOWER:
            default:
                return pos;
        }
    }

    private void propagateState(final World world, final BlockPos lowerPos,
            final BlockState lowerState, final net.minecraft.state.IProperty<?> prop,
            final Object value) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final BlockState newLower = lowerState.with((net.minecraft.state.IProperty) prop, (Comparable) value);
        world.setBlockState(lowerPos, newLower, 10);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final BlockState newMiddle = world.getBlockState(lowerPos.up()).with(
                (net.minecraft.state.IProperty) prop, (Comparable) value);
        world.setBlockState(lowerPos.up(), newMiddle, 10);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final BlockState newUpper = world.getBlockState(lowerPos.up(2)).with(
                (net.minecraft.state.IProperty) prop, (Comparable) value);
        world.setBlockState(lowerPos.up(2), newUpper, 10);
    }

    private void removeAllParts(final World world, final BlockPos pos, final BlockState state) {
        final BlockPos lower = lowerPosOf(state, pos);
        for (int dy = 0; dy < 3; dy++) {
            final BlockPos p = lower.up(dy);
            if (p.equals(pos)) {
                continue; // wird vom regulaeren Break-Pfad selbst entfernt
            }
            if (world.getBlockState(p).getBlock() instanceof TCBigDoor) {
                world.setBlockState(p, net.minecraft.block.Blocks.AIR.getDefaultState(), 35);
            }
        }
    }

    private static int openSoundEvent(final BlockState state) {
        return state.getMaterial() == Material.IRON ? 1005 : 1006;
    }

    private static int closeSoundEvent(final BlockState state) {
        return state.getMaterial() == Material.IRON ? 1011 : 1012;
    }

    @Nullable
    public SoundEvent getOpenSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    }

    @Nullable
    public SoundEvent getCloseSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        // Das LOWER-Block-Placement uebernimmt der TCBigDoorItem; hier
        // returnen wir den default mit FACING in Blickrichtung des Setzers
        // (1.12-Original macht's genauso, kein .getOpposite()).
        return this.getDefaultState().with(FACING,
                context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return mirror == Mirror.NONE
                ? state
                : state.rotate(mirror.toRotation(state.get(FACING)))
                        .with(HINGE,
                                state.get(HINGE) == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT
                                        : DoorHingeSide.LEFT);
    }

    @Override
    public boolean canSpawnInBlock() {
        return false;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HINGE, POWERED, THIRD);
    }

    /** Erlaubt Mobs/Iron-Golems das Tor nicht zu durchbrechen, wenn closed. */
    public boolean isOpen(final BlockState state) {
        return state.get(OPEN);
    }

}
