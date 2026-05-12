package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

/**
 * Pane-artiges Fenster mit zusaetzlicher vertikaler Verbindung. Zustaende
 * spiegeln das 1.12.2-Original 1:1:
 *
 * <ul>
 *   <li>{@code x} (boolean) -- Pane-Achse: true = X-axis (Pane verlaeuft
 *       entlang X), false = Z-axis</li>
 *   <li>{@code up} / {@code down} -- vertikale Verbindung an einen
 *       TCWindow-Nachbarn (oben/unten)</li>
 *   <li>{@code left} / {@code right} -- Verbindung entlang der Pane-Achse;
 *       fuer X-Pane sind das West/East-Nachbarn, fuer Z-Pane North/South</li>
 * </ul>
 *
 * Die Verbindungslogik checkt nur, ob der Nachbar selbst ein TCWindow ist,
 * unabhaengig von dessen Achse -- analog zur 1.12.2-{@code attachesToBlock}.
 */
public class TCWindow extends Block {

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    /** {@code x}: true = pane verlaeuft entlang X-Axis, false = Z-Axis. */
    public static final BooleanProperty AXIS_X = BooleanProperty.create("x");

    private static final VoxelShape SHAPE_X = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_Z = Block.box(7, 0, 0, 9, 16, 16);

    public TCWindow(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE)
                .setValue(LEFT, Boolean.FALSE)
                .setValue(RIGHT, Boolean.FALSE)
                .setValue(AXIS_X, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world,
            final BlockPos pos, final CollisionContext context) {
        return state.getValue(AXIS_X) ? SHAPE_X : SHAPE_Z;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        // Spieler schaut entlang Z -> Pane steht senkrecht dazu auf X-Achse.
        final Direction facing = context.getHorizontalDirection();
        final boolean axisX = facing.getAxis() == Direction.Axis.Z;
        return computeConnections(defaultBlockState().setValue(AXIS_X, axisX),
                context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(final BlockState state, final Direction facing,
            final BlockState facingState, final LevelAccessor world, final BlockPos currentPos,
            final BlockPos facingPos) {
        return computeConnections(state, world, currentPos);
    }

    private BlockState computeConnections(final BlockState state, final BlockGetter world,
            final BlockPos pos) {
        final boolean axisX = state.getValue(AXIS_X);
        final Direction left = axisX ? Direction.WEST : Direction.NORTH;
        final Direction right = axisX ? Direction.EAST : Direction.SOUTH;
        return state
                .setValue(UP, attaches(world.getBlockState(pos.above())))
                .setValue(DOWN, attaches(world.getBlockState(pos.below())))
                .setValue(LEFT, attaches(world.getBlockState(pos.relative(left))))
                .setValue(RIGHT, attaches(world.getBlockState(pos.relative(right))));
    }

    private static boolean attaches(final BlockState neighbor) {
        return neighbor.getBlock() instanceof TCWindow;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, LEFT, RIGHT, AXIS_X);
    }
}
