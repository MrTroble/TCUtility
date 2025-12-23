package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCWindow extends TCCube {

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool FACING = PropertyBool.create("x");
    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");

    protected static final AxisAlignedBB AABB_X =
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D);
    protected static final AxisAlignedBB AABB_Z =
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D);

    public TCWindow(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.SPECIAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false)
                .withProperty(DOWN, false).withProperty(FACING, false).withProperty(LEFT, false)
                .withProperty(RIGHT, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState finalstate, final IBlockAccess source,
            final BlockPos pos) {
        final IBlockState state = this.getActualState(finalstate, source, pos);
        return state.getValue(FACING) ? AABB_X : AABB_Z;
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
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public IBlockState getActualState(final IBlockState finalstate, final IBlockAccess worldIn,
            final BlockPos pos) {
        IBlockState state = finalstate;
        state = state.withProperty(UP, this.attachesToBlock(worldIn, pos.up()));
        state = state.withProperty(DOWN, this.attachesToBlock(worldIn, pos.down()));
        if (state.getValue(FACING)) {
            state = state.withProperty(LEFT, this.attachesToBlock(worldIn, pos.west()));
            state = state.withProperty(RIGHT, this.attachesToBlock(worldIn, pos.east()));
        } else {
            state = state.withProperty(LEFT, this.attachesToBlock(worldIn, pos.north()));
            state = state.withProperty(RIGHT, this.attachesToBlock(worldIn, pos.south()));
        }
        return state;
    }

    @Override
    public IBlockState getStateForPlacement(final World world, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer, final EnumHand hand) {
        final Axis facingAxis = placer.getHorizontalFacing().getAxis();
        return facingAxis.equals(EnumFacing.Axis.X)
                ? this.getDefaultState().withProperty(FACING, false)
                : this.getDefaultState().withProperty(FACING, true);
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return meta == 1 ? this.getDefaultState().withProperty(FACING, true)
                : this.getDefaultState().withProperty(FACING, false);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue(FACING).booleanValue()) {
            i = 1;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, FACING, LEFT, RIGHT);
    }

    private boolean attachesToBlock(final IBlockAccess world, final BlockPos pos) {
        final Block block = world.getBlockState(pos).getBlock();
        return block instanceof TCWindow;
    }

}
