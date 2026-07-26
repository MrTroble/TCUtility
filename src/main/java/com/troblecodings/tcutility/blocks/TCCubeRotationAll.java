package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class TCCubeRotationAll extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private final VoxelShape yShape;
    private final VoxelShape xShape;
    private final VoxelShape zShape;
    private final BlockRenderLayer renderLayer;

    public TCCubeRotationAll(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.renderLayer = TCCube.layerFor(blockInfo.material);
        final int[] b = TCCube.boxArr(blockInfo.box);
        // Y-Achse ist Default; X / Z sind Drehungen um die jeweilige Achse:
        //   X-Achse: y/z werden getauscht und gespiegelt
        //   Z-Achse: x/y werden getauscht und gespiegelt
        this.yShape = Block.makeCuboidShape(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.xShape = Block.makeCuboidShape(b[0], b[2], 16 - b[4], b[3], b[5], 16 - b[1]);
        this.zShape = Block.makeCuboidShape(b[1], 16 - b[3], b[2], b[4], 16 - b[0], b[5]);

        this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos,
            final ISelectionContext context) {
        switch (state.get(AXIS)) {
            case X:
                return xShape;
            case Z:
                return zShape;
            case Y:
            default:
                return yShape;
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.renderLayer;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.getDefaultState().with(AXIS, context.getFace().getAxis());
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        if (state.get(AXIS) == Direction.Axis.Y) {
            return state;
        }
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return state.with(AXIS,
                        state.get(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return state;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
