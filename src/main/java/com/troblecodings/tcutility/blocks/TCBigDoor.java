package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.MaterialKind;
import com.troblecodings.tcutility.utils.MaterialKindRegistry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * Drei-Block-tall Door-Variante. Im Gegensatz zum 1.14-Vanilla-DoorBlock
 * (zwei Blocks: LOWER/UPPER) trackt {@link #THIRD} drei Stockwerke
 * (LOWER/MIDDLE/UPPER); Open- und Power-State liegen am untersten Block,
 * die anderen zwei spiegeln den State.
 */
public class TCBigDoor extends Block {

    public enum BigDoorThird implements StringRepresentable {
        LOWER("lower"), MIDDLE("middle"), UPPER("upper");

        private final String name;

        BigDoorThird(final String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static final net.minecraft.world.level.block.state.properties.EnumProperty<net.minecraft.core.Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
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
    private static final VoxelShape SOUTH_L_AABB = Block.box(0, 0, 0, 24, 16, 3);
    private static final VoxelShape NORTH_L_AABB = Block.box(0, 0, 13, 24, 16, 16);
    private static final VoxelShape WEST_L_AABB = Block.box(13, 0, 0, 16, 16, 24);
    private static final VoxelShape EAST_L_AABB = Block.box(0, 0, 0, 3, 16, 24);
    private static final VoxelShape SOUTH_R_AABB = Block.box(-8, 0, 0, 16, 16, 3);
    private static final VoxelShape NORTH_R_AABB = Block.box(-8, 0, 13, 16, 16, 16);
    private static final VoxelShape WEST_R_AABB = Block.box(13, 0, -8, 16, 16, 16);
    private static final VoxelShape EAST_R_AABB = Block.box(0, 0, -8, 3, 16, 16);

    private Item item;

    public TCBigDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, Boolean.FALSE)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(POWERED, Boolean.FALSE)
                .setValue(THIRD, BigDoorThird.LOWER));
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world,
            final BlockPos pos, final CollisionContext context) {
        // 1:1-Port aus 1.12 BlockBigDoor.getBoundingBox: 4 Facings * 2 Hinges
        // * 2 Open-States = 16 Kombinationen, alle auf 8 unterschiedliche
        // 1,5-Block-AABBs gemappt. closed=!OPEN, rightHinge=HINGE==RIGHT.
        final boolean closed = !state.getValue(OPEN);
        final boolean rightHinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch (state.getValue(FACING)) {
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
    protected InteractionResult useWithoutItem(final BlockState state, final Level world,
            final BlockPos pos, final Player player, final BlockHitResult hit) {
        if (MaterialKindRegistry.get(state.getBlock()) == MaterialKind.METAL) {
            return InteractionResult.PASS;
        }
        final BlockPos lowerPos = lowerPosOf(state, pos);
        final BlockState lowerState = world.getBlockState(lowerPos);
        if (!(lowerState.getBlock() instanceof TCBigDoor)) {
            return InteractionResult.PASS;
        }
        final boolean newOpen = !lowerState.getValue(OPEN);
        propagateState(world, lowerPos, lowerState, OPEN, newOpen);
        world.levelEvent(player, newOpen ? openSoundEvent(state) : closeSoundEvent(state),
                pos, 0);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(final BlockState state, final Level world, final BlockPos pos,
            final Block fromBlock, final net.minecraft.world.level.redstone.Orientation orientation,
            final boolean isMoving) {
        // 1:1-Port aus 1.12 BlockDoor.neighborChanged: UPPER und MIDDLE
        // pruefen Struktur-Integritaet und delegieren Redstone an LOWER;
        // LOWER pruefst Untergrund + alle drei Glieder, dropped beim
        // Wegbrechen, und schaltet Powered/Open synchron auf Redstone.
        // 1.21.4: handleNeighborChanged nimmt Orientation statt BlockPos.
        switch (state.getValue(THIRD)) {
            case UPPER: {
                final BlockPos middle = pos.below();
                final BlockPos lower = pos.below(2);
                final BlockState middleState = world.getBlockState(middle);
                final BlockState lowerState = world.getBlockState(lower);
                if (!(middleState.getBlock() instanceof TCBigDoor)
                        || !(lowerState.getBlock() instanceof TCBigDoor)) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
                } else if (!(fromBlock instanceof TCBigDoor)) {
                    middleState.handleNeighborChanged(world, middle, fromBlock, orientation, isMoving);
                }
                return;
            }
            case MIDDLE: {
                final BlockPos lower = pos.below();
                final BlockPos upper = pos.above();
                final BlockState lowerState = world.getBlockState(lower);
                final BlockState upperState = world.getBlockState(upper);
                if (!(lowerState.getBlock() instanceof TCBigDoor)
                        || !(upperState.getBlock() instanceof TCBigDoor)) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
                } else if (!(fromBlock instanceof TCBigDoor)) {
                    lowerState.handleNeighborChanged(world, lower, fromBlock, orientation, isMoving);
                }
                return;
            }
            case LOWER:
            default:
                break;
        }
        // LOWER-Pfad: Struktur und Redstone.
        final BlockPos middle = pos.above();
        final BlockPos upper = pos.above(2);
        final BlockState middleState = world.getBlockState(middle);
        final BlockState upperState = world.getBlockState(upper);
        boolean broken = false;
        if (!(middleState.getBlock() instanceof TCBigDoor)) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            broken = true;
        }
        if (!(upperState.getBlock() instanceof TCBigDoor)) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            broken = true;
        }
        if (!world.getBlockState(pos.below()).isSolid()) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            broken = true;
            if (middleState.getBlock() instanceof TCBigDoor) {
                world.setBlock(middle, Blocks.AIR.defaultBlockState(), 35);
            }
            if (upperState.getBlock() instanceof TCBigDoor) {
                world.setBlock(upper, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        if (broken) {
            if (!world.isClientSide && this.item != null) {
                Block.popResource(world, pos, new ItemStack(this.item));
            }
            return;
        }
        final boolean powered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(middle)
                || world.hasNeighborSignal(upper);
        if (!(fromBlock instanceof TCBigDoor)
                && (powered || fromBlock.defaultBlockState().isSignalSource())
                && powered != state.getValue(POWERED)) {
            BlockState s = state.setValue(POWERED, powered);
            if (powered != state.getValue(OPEN)) {
                s = s.setValue(OPEN, powered);
                world.levelEvent(null, powered ? openSoundEvent(s) : closeSoundEvent(s), pos, 0);
            }
            propagateState(world, pos, s, POWERED, powered);
            propagateState(world, pos, s, OPEN, s.getValue(OPEN));
        }
    }

    @Override
    public BlockState playerWillDestroy(final Level world, final BlockPos pos, final BlockState state,
            final Player player) {
        if (player.getAbilities().instabuild) {
            removeAllParts(world, pos, state);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onRemove(final BlockState state, final Level world, final BlockPos pos,
            final BlockState newState, final boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            // Wenn der Block via Explosion / setBlockState aus der Welt
            // verschwindet (nicht via playerWillDestroy), trotzdem die
            // anderen zwei Glieder nachreissen.
            removeAllParts(world, pos, state);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    /**
     * Position des LOWER-Blocks unseres 3er-Clusters relativ zu {@code pos}.
     */
    public static BlockPos lowerPosOf(final BlockState state, final BlockPos pos) {
        switch (state.getValue(THIRD)) {
            case UPPER:
                return pos.below(2);
            case MIDDLE:
                return pos.below();
            case LOWER:
            default:
                return pos;
        }
    }

    private void propagateState(final Level world, final BlockPos lowerPos,
            final BlockState lowerState,
            final net.minecraft.world.level.block.state.properties.Property<?> prop,
            final Object value) {
        // Defensiv: nur auf TCBigDoor-Bloecke setzen. Falls Middle/Upper aus
        // einem Strukturbruch heraus fehlen oder schon Air sind, wuerde
        // setValue auf einer Air-State NPE/IllegalArgument werfen.
        applyTo(world, lowerPos, lowerState, prop, value);
        applyTo(world, lowerPos.above(), world.getBlockState(lowerPos.above()), prop, value);
        applyTo(world, lowerPos.above(2), world.getBlockState(lowerPos.above(2)), prop, value);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void applyTo(final Level world, final BlockPos pos, final BlockState state,
            final net.minecraft.world.level.block.state.properties.Property<?> prop,
            final Object value) {
        if (!(state.getBlock() instanceof TCBigDoor)) {
            return;
        }
        if (!state.hasProperty((net.minecraft.world.level.block.state.properties.Property) prop)) {
            return;
        }
        final BlockState updated = state.setValue(
                (net.minecraft.world.level.block.state.properties.Property) prop,
                (Comparable) value);
        world.setBlock(pos, updated, 10);
    }

    private void removeAllParts(final Level world, final BlockPos pos, final BlockState state) {
        final BlockPos lower = lowerPosOf(state, pos);
        for (int dy = 0; dy < 3; dy++) {
            final BlockPos p = lower.above(dy);
            if (p.equals(pos)) {
                continue; // wird vom regulaeren Break-Pfad selbst entfernt
            }
            if (world.getBlockState(p).getBlock() instanceof TCBigDoor) {
                world.setBlock(p, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
        }
    }

    private static int openSoundEvent(final BlockState state) {
        return MaterialKindRegistry.get(state.getBlock()) == MaterialKind.METAL ? 1005 : 1006;
    }

    private static int closeSoundEvent(final BlockState state) {
        return MaterialKindRegistry.get(state.getBlock()) == MaterialKind.METAL ? 1011 : 1012;
    }

    @Nullable
    public SoundEvent getOpenSound() {
        return SoundEvents.WOODEN_DOOR_OPEN;
    }

    @Nullable
    public SoundEvent getCloseSound() {
        return SoundEvents.WOODEN_DOOR_CLOSE;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        // Das LOWER-Block-Placement uebernimmt der TCBigDoorItem; hier
        // returnen wir den default mit FACING in Blickrichtung des Setzers
        // (1.12-Original macht's genauso, kein .getOpposite()).
        return this.defaultBlockState().setValue(FACING,
                context.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return mirror == Mirror.NONE
                ? state
                : state.rotate(mirror.getRotation(state.getValue(FACING)))
                        .setValue(HINGE,
                                state.getValue(HINGE) == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT
                                        : DoorHingeSide.LEFT);
    }

    @Override
    public boolean isPossibleToRespawnInThis(final BlockState state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HINGE, POWERED, THIRD);
    }

    /** Erlaubt Mobs/Iron-Golems das Tor nicht zu durchbrechen, wenn closed. */
    public boolean isOpen(final BlockState state) {
        return state.getValue(OPEN);
    }

}
