package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

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

    private static final VoxelShape SHAPE_X = Block.makeCuboidShape(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_Z = Block.makeCuboidShape(7, 0, 0, 9, 16, 16);

    public TCWindow(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(UP, Boolean.FALSE)
                .with(DOWN, Boolean.FALSE)
                .with(LEFT, Boolean.FALSE)
                .with(RIGHT, Boolean.FALSE)
                .with(AXIS_X, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world,
            final BlockPos pos, final ISelectionContext context) {
        return state.get(AXIS_X) ? SHAPE_X : SHAPE_Z;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        // Spieler schaut entlang Z -> Pane steht senkrecht dazu auf X-Achse.
        final Direction facing = context.getPlacementHorizontalFacing();
        final boolean axisX = facing.getAxis() == Direction.Axis.Z;
        return computeConnections(getDefaultState().with(AXIS_X, axisX),
                context.getWorld(), context.getPos());
    }

    @Override
    public BlockState updatePostPlacement(final BlockState state, final Direction facing,
            final BlockState facingState, final IWorld world, final BlockPos currentPos,
            final BlockPos facingPos) {
        return computeConnections(state, world, currentPos);
    }

    private BlockState computeConnections(final BlockState state, final IBlockReader world,
            final BlockPos pos) {
        final boolean axisX = state.get(AXIS_X);
        final Direction left = axisX ? Direction.WEST : Direction.NORTH;
        final Direction right = axisX ? Direction.EAST : Direction.SOUTH;
        return state
                .with(UP, attaches(world.getBlockState(pos.up())))
                .with(DOWN, attaches(world.getBlockState(pos.down())))
                .with(LEFT, attaches(world.getBlockState(pos.offset(left))))
                .with(RIGHT, attaches(world.getBlockState(pos.offset(right))));
    }

    private static boolean attaches(final BlockState neighbor) {
        return neighbor.getBlock() instanceof TCWindow;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, LEFT, RIGHT, AXIS_X);
    }
}
