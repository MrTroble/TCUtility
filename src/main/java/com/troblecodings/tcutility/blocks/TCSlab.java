package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCSlab extends TCCube {

    public static final PropertyEnum<TCSlab.EnumBlockHalf> HALF = PropertyEnum.<TCSlab.EnumBlockHalf>create(
            "half", TCSlab.EnumBlockHalf.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D,
            1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D,
            1.0D, 1.0D);

    public TCSlab(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        IBlockState iblockstate = this.blockState.getBaseState();
        iblockstate = iblockstate.withProperty(HALF, TCSlab.EnumBlockHalf.BOTTOM);
        this.setCreativeTab(TCTabs.SLABS);
        this.setDefaultState(iblockstate);
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        return state.getValue(HALF) == TCSlab.EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
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
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY,
                hitZ, meta, placer).withProperty(HALF, TCSlab.EnumBlockHalf.BOTTOM);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D) ? iblockstate
                : iblockstate.withProperty(HALF, TCSlab.EnumBlockHalf.TOP);
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(HALF,
                (meta & 8) == 0 ? TCSlab.EnumBlockHalf.BOTTOM : TCSlab.EnumBlockHalf.TOP);
        return iblockstate;
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue(HALF) == TCSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                HALF
        });
    }

    public static enum EnumBlockHalf implements IStringSerializable {
        TOP, BOTTOM;

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == BOTTOM ? "bottom" : "top";
        }
    }
}
