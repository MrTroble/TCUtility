package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCFenceGate extends TCCubeRotation {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D,
            1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D,
            0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS_INWALL = new AxisAlignedBB(0.0D, 0.0D,
            0.375D, 1.0D, 0.8125D, 0.625D);
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS_INWALL = new AxisAlignedBB(0.375D, 0.0D,
            0.0D, 0.625D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_COLLISION_BOX_ZAXIS = new AxisAlignedBB(0.0D, 0.0D,
            0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB AABB_COLLISION_BOX_XAXIS = new AxisAlignedBB(0.375D, 0.0D,
            0.0D, 0.625D, 1.5D, 1.0D);

    public TCFenceGate(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.FENCE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = this.getActualState(state, source, pos);

        if (state.getValue(IN_WALL).booleanValue()) {
            return state.getValue(FACING).getAxis() == EnumFacing.Axis.X
                    ? AABB_HITBOX_XAXIS_INWALL
                    : AABB_HITBOX_ZAXIS_INWALL;
        } else {
            return state.getValue(FACING).getAxis() == EnumFacing.Axis.X
                    ? AABB_HITBOX_XAXIS
                    : AABB_HITBOX_ZAXIS;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis enumfacing$axis = state.getValue(FACING).getAxis();

        if (enumfacing$axis == EnumFacing.Axis.Z
                && (worldIn.getBlockState(pos.west()).getBlock() instanceof BlockWall
                        || worldIn.getBlockState(pos.east()).getBlock() instanceof BlockWall)
                || enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north())
                        .getBlock() instanceof BlockWall
                        || worldIn.getBlockState(pos.south()).getBlock() instanceof BlockWall)) {
            state = state.withProperty(IN_WALL, Boolean.valueOf(true));
        }

        return state;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid()
                ? super.canPlaceBlockAt(worldIn, pos)
                : false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn,
            final BlockPos pos) {
        if (blockState.getValue(OPEN).booleanValue()) {
            return NULL_AABB;
        } else {
            return blockState.getValue(FACING).getAxis() == EnumFacing.Axis.Z
                    ? AABB_COLLISION_BOX_ZAXIS
                    : AABB_COLLISION_BOX_XAXIS;
        }
    }

    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(OPEN).booleanValue();
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing,
            final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final boolean flag = worldIn.isBlockPowered(pos);
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(OPEN, Boolean.valueOf(flag))
                .withProperty(POWERED, Boolean.valueOf(flag))
                .withProperty(IN_WALL, Boolean.valueOf(false));
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state,
            final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
            final float hitZ) {
        if (state.getValue(OPEN).booleanValue()) {
            state = state.withProperty(OPEN, Boolean.valueOf(false));
            worldIn.setBlockState(pos, state, 10);
        } else {
            final EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);

            if (state.getValue(FACING) == enumfacing.getOpposite()) {
                state = state.withProperty(FACING, enumfacing);
            }

            state = state.withProperty(OPEN, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 10);
        }

        worldIn.playEvent(playerIn, state.getValue(OPEN).booleanValue() ? 1008 : 1014,
                pos, 0);
        return true;
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn,
            final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);

            if (state.getValue(POWERED).booleanValue() != flag) {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag))
                        .withProperty(OPEN, Boolean.valueOf(flag)), 2);

                if (state.getValue(OPEN).booleanValue() != flag) {
                    worldIn.playEvent((EntityPlayer) null, flag ? 1008 : 1014, pos, 0);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess,
            final BlockPos pos, final EnumFacing side) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta))
                .withProperty(OPEN, Boolean.valueOf((meta & 4) != 0))
                .withProperty(POWERED, Boolean.valueOf((meta & 8) != 0));
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }

        if (state.getValue(OPEN).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FACING, OPEN, POWERED, IN_WALL
        });
    }

    @Override
    public boolean canBeConnectedTo(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {
        final IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFenceGate
                && state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.MIDDLE_POLE) {
            final Block connector = world.getBlockState(pos.offset(facing)).getBlock();
            return connector instanceof BlockFence || connector instanceof BlockWall;
        }
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos,
            final EnumFacing face) {
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            return state.getValue(FACING).getAxis() == face.rotateY().getAxis()
                    ? BlockFaceShape.MIDDLE_POLE
                    : BlockFaceShape.UNDEFINED;
        } else {
            return BlockFaceShape.UNDEFINED;
        }
    }
}
