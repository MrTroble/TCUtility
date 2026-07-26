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

/**
 * Block, der an jeder Seite (inkl. Decke / Boden) angebracht werden kann; die
 * Hitbox aus {@link BlockCreateInfo#box} wird passend zur Orientierung gedreht.
 */
public class TCHanging extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private final VoxelShape upShape;
    private final VoxelShape downShape;
    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;
    private final BlockRenderLayer renderLayer;

    public TCHanging(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.renderLayer = TCCube.layerFor(blockInfo.material);
        final int[] b = TCCube.boxArr(blockInfo.box);
        // Default-Orientierung ist UP (Block haengt von der Decke). Die anderen
        // Orientierungen werden ueber Y- bzw. X-Rotationen abgeleitet.
        this.upShape = Block.makeCuboidShape(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.downShape = Block.makeCuboidShape(b[0], 16 - b[4], b[2], b[3], 16 - b[1], b[5]);
        this.northShape = Block.makeCuboidShape(b[0], b[2], 16 - b[4], b[3], b[5], 16 - b[1]);
        this.southShape = Block.makeCuboidShape(b[0], b[2], b[1], b[3], b[5], b[4]);
        this.eastShape = Block.makeCuboidShape(b[1], b[2], b[0], b[4], b[5], b[3]);
        this.westShape = Block.makeCuboidShape(16 - b[4], b[2], b[0], 16 - b[1], b[5], b[3]);

        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos,
            final ISelectionContext context) {
        switch (state.get(FACING)) {
            case DOWN:
                return downShape;
            case NORTH:
                return northShape;
            case SOUTH:
                return southShape;
            case EAST:
                return eastShape;
            case WEST:
                return westShape;
            case UP:
            default:
                return upShape;
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.renderLayer;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
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
