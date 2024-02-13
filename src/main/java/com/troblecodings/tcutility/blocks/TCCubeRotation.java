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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCCubeRotation extends TCCube {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    private final AxisAlignedBB northBB = new AxisAlignedBB(getIndexBox(0) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(2) * 0.0625, getIndexBox(3) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(5) * 0.0625);
    private final AxisAlignedBB eastBB = new AxisAlignedBB(1 - getIndexBox(2) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(0) * 0.0625, 1 - getIndexBox(5) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(3) * 0.0625);
    private final AxisAlignedBB southBB = new AxisAlignedBB(getIndexBox(0) * 0.0625,
            getIndexBox(1) * 0.0625, 1 - getIndexBox(2) * 0.0625, getIndexBox(3) * 0.0625,
            getIndexBox(4) * 0.0625, 1 - getIndexBox(5) * 0.0625);
    private final AxisAlignedBB westBB = new AxisAlignedBB(getIndexBox(2) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(0) * 0.0625, getIndexBox(5) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(3) * 0.0625);

    public TCCubeRotation(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

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

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState finalstate, final IBlockAccess source,
            final BlockPos pos) {
        IBlockState state = this.getActualState(finalstate, source, pos);
        final EnumFacing enumFacing = state.getValue(FACING);

        switch (enumFacing) {
            case NORTH:
            default:
                return northBB;
            case EAST:
                return eastBB;
            case SOUTH:
                return southBB;
            case WEST:
                return westBB;
        }
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
