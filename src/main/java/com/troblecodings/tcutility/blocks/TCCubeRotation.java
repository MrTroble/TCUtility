package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
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

public class TCCubeRotation extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;
    private final BlockRenderLayer renderLayer;

    public TCCubeRotation(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.renderLayer = TCCube.layerFor(blockInfo.material);
        final int[] b = TCCube.boxArr(blockInfo.box);
        // box ist im 0..16 Bereich; Default ist [0,0,0,16,16,16].
        // North = unmodifiziert, Rotation um Y-Achse:
        //   East  = 90° im Uhrzeigersinn  -> (16-z2, y1, x1, 16-z1, y2, x2)
        //   South = 180°                  -> (16-x2, y1, 16-z2, 16-x1, y2, 16-z1)
        //   West  = 270° im Uhrzeigersinn -> (z1, y1, 16-x2, z2, y2, 16-x1)
        this.northShape = Block.makeCuboidShape(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.eastShape = Block.makeCuboidShape(16 - b[5], b[1], b[0], 16 - b[2], b[4], b[3]);
        this.southShape = Block.makeCuboidShape(16 - b[3], b[1], 16 - b[5],
                16 - b[0], b[4], 16 - b[2]);
        this.westShape = Block.makeCuboidShape(b[2], b[1], 16 - b[3], b[5], b[4], 16 - b[0]);

        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos,
            final ISelectionContext context) {
        switch (state.get(FACING)) {
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
    public BlockRenderLayer getRenderLayer() {
        return this.renderLayer;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.getDefaultState().with(FACING,
                context.getPlacementHorizontalFacing().getOpposite());
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
        builder.add(FACING);
    }
}
