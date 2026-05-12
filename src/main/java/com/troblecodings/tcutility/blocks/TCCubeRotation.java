package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

public class TCCubeRotation extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;

    public TCCubeRotation(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        final int[] b = TCCube.boxArr(blockInfo.box);
        // box ist im 0..16 Bereich; Default ist [0,0,0,16,16,16].
        // North = unmodifiziert, Rotation um Y-Achse:
        //   East  = 90° im Uhrzeigersinn  -> (16-z2, y1, x1, 16-z1, y2, x2)
        //   South = 180°                  -> (16-x2, y1, 16-z2, 16-x1, y2, 16-z1)
        //   West  = 270° im Uhrzeigersinn -> (z1, y1, 16-x2, z2, y2, 16-x1)
        this.northShape = Block.box(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.eastShape = Block.box(16 - b[5], b[1], b[0], 16 - b[2], b[4], b[3]);
        this.southShape = Block.box(16 - b[3], b[1], 16 - b[5],
                16 - b[0], b[4], 16 - b[2]);
        this.westShape = Block.box(b[2], b[1], 16 - b[3], b[5], b[4], 16 - b[0]);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos,
            final CollisionContext context) {
        switch (state.getValue(FACING)) {
            case EAST:
                return eastShape;
            case SOUTH:
                return southShape;
            case WEST:
                return westShape;
            case NORTH:
            default:
                return northShape;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING,
                context.getHorizontalDirection().getOpposite());
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
        builder.add(FACING);
    }
}
