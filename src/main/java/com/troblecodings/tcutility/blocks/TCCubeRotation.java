package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TCCubeRotation extends TCCube {

    public TCCubeRotation(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    @Override
    public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
        return layer.equals(BlockRenderLayer.CUTOUT_MIPPED);
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(final World world, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer, final EnumHand hand) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

}
