package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCLadder extends TCCube {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected static final AxisAlignedBB LADDER_EAST_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB =
            new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);

    public TCLadder(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.SPECIAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return LADDER_NORTH_AABB;
            case SOUTH:
                return LADDER_SOUTH_AABB;
            case WEST:
                return LADDER_WEST_AABB;
            case EAST:
            default:
                return LADDER_EAST_AABB;
        }
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos,
            final EnumFacing side) {
        if (this.canAttachTo(worldIn, pos.west(), side))
            return true;
        else if (this.canAttachTo(worldIn, pos.east(), side))
            return true;
        else if (this.canAttachTo(worldIn, pos.north(), side))
            return true;
        else
            return this.canAttachTo(worldIn, pos.south(), side);
    }

    private boolean canAttachTo(final World worldIn, final BlockPos blockPos,
            final EnumFacing facing) {
        final IBlockState iblockstate = worldIn.getBlockState(blockPos);
        final boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
        return !flag
                && iblockstate.getBlockFaceShape(worldIn, blockPos, facing) == BlockFaceShape.SOLID
                && !iblockstate.canProvidePower();
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal()
                && this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing))
            return this.getDefaultState().withProperty(FACING, facing);
        else {
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing))
                    return this.getDefaultState().withProperty(FACING, enumfacing);
            }

            return this.getDefaultState();
        }
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        final EnumFacing enumfacing = state.getValue(FACING);

        if (!this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isLadder(final IBlockState state, final IBlockAccess world, final BlockPos pos,
            final EntityLivingBase entity) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FACING
        });
    }
}
