package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCCubeRotationAll extends TCCube {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis>create(
            "axis", EnumFacing.Axis.class);

    public TCCubeRotationAll(BlockCreateInfo blockInfo) {
        super(blockInfo);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet()) {
            if (prop.getName().equals("axis")) {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState finalstate, final IBlockAccess source,
            final BlockPos pos) {
        final IBlockState state = this.getActualState(finalstate, source, pos);
        AxisAlignedBB bb = FULL_BLOCK_AABB;
        if (state.getValue(AXIS).equals(EnumFacing.Axis.X)) {
            bb = new AxisAlignedBB(getIndexBox(2) * 0.0625, getIndexBox(1) * 0.0625,
                    getIndexBox(0) * 0.0625, getIndexBox(5) * 0.0625,
                    getIndexBox(4) * 0.0625, getIndexBox(3) * 0.0625);
        } else if (state.getValue(AXIS).equals(EnumFacing.Axis.Z)) {
            bb = new AxisAlignedBB(getIndexBox(0) * 0.0625, getIndexBox(1) * 0.0625,
                    getIndexBox(2) * 0.0625, getIndexBox(3) * 0.0625,
                    getIndexBox(4) * 0.0625, getIndexBox(5) * 0.0625);
        } else if (state.getValue(AXIS).equals(EnumFacing.Axis.Y)) {
            bb = new AxisAlignedBB(getIndexBox(1) * 0.0625, getIndexBox(0) * 0.0625,
                    getIndexBox(2) * 0.0625, getIndexBox(4) * 0.0625,
                    getIndexBox(3) * 0.0625, getIndexBox(5) * 0.0625);
        }
        return bb;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_90:
            case COUNTERCLOCKWISE_90:
                switch ((EnumFacing.Axis) state.getValue(AXIS)) {
                    case X:
                        return state.withProperty(AXIS, EnumFacing.Axis.Z);
                    case Z:
                        return state.withProperty(AXIS, EnumFacing.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis facingAxis = EnumFacing.Axis.Y;
        int i = meta & 12;

        if (i == 4) {
            facingAxis = EnumFacing.Axis.X;
        } else if (i == 8) {
            facingAxis = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(AXIS, facingAxis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        EnumFacing.Axis facingAxis = (EnumFacing.Axis) state.getValue(AXIS);

        if (facingAxis == EnumFacing.Axis.X) {
            i |= 4;
        } else if (facingAxis == EnumFacing.Axis.Z) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                AXIS
        });
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(AXIS, facing.getAxis());
    }

}
