package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

public class TCCubeRotationAll extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private final VoxelShape yShape;
    private final VoxelShape xShape;
    private final VoxelShape zShape;

    public TCCubeRotationAll(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        final int[] b = TCCube.boxArr(blockInfo.box);
        // Y-Achse ist Default; X / Z sind Drehungen um die jeweilige Achse:
        //   X-Achse: y/z werden getauscht und gespiegelt
        //   Z-Achse: x/y werden getauscht und gespiegelt
        this.yShape = Block.box(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.xShape = Block.box(b[0], b[2], 16 - b[4], b[3], b[5], 16 - b[1]);
        this.zShape = Block.box(b[1], 16 - b[3], b[2], b[4], 16 - b[0], b[5]);

        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos,
            final CollisionContext context) {
        switch (state.getValue(AXIS)) {
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
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        if (state.getValue(AXIS) == Direction.Axis.Y) {
            return state;
        }
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return state.setValue(AXIS,
                        state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return state;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
