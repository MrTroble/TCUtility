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

    public TCHanging(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
        final int[] b = TCCube.boxArr(blockInfo.box);
        // Default-Orientierung ist UP (Block haengt von der Decke). Die anderen
        // Orientierungen werden ueber Y- bzw. X-Rotationen abgeleitet.
        this.upShape = Block.box(b[0], b[1], b[2], b[3], b[4], b[5]);
        this.downShape = Block.box(b[0], 16 - b[4], b[2], b[3], 16 - b[1], b[5]);
        this.northShape = Block.box(b[0], b[2], 16 - b[4], b[3], b[5], 16 - b[1]);
        this.southShape = Block.box(b[0], b[2], b[1], b[3], b[5], b[4]);
        this.eastShape = Block.box(b[1], b[2], b[0], b[4], b[5], b[3]);
        this.westShape = Block.box(16 - b[4], b[2], b[0], 16 - b[1], b[5], b[3]);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos,
            final CollisionContext context) {
        switch (state.getValue(FACING)) {
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
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
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
