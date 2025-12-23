package com.troblecodings.tcutility.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCWall extends TCFence {

    public static final PropertyBool UP = PropertyBool.create("up");

    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] {
            AABB_BY_INDEX[0].setMaxY(1.5D), AABB_BY_INDEX[1].setMaxY(1.5D),
            AABB_BY_INDEX[2].setMaxY(1.5D), AABB_BY_INDEX[3].setMaxY(1.5D),
            AABB_BY_INDEX[4].setMaxY(1.5D), AABB_BY_INDEX[5].setMaxY(1.5D),
            AABB_BY_INDEX[6].setMaxY(1.5D), AABB_BY_INDEX[7].setMaxY(1.5D),
            AABB_BY_INDEX[8].setMaxY(1.5D), AABB_BY_INDEX[9].setMaxY(1.5D),
            AABB_BY_INDEX[10].setMaxY(1.5D), AABB_BY_INDEX[11].setMaxY(1.5D),
            AABB_BY_INDEX[12].setMaxY(1.5D), AABB_BY_INDEX[13].setMaxY(1.5D),
            AABB_BY_INDEX[14].setMaxY(1.5D), AABB_BY_INDEX[15].setMaxY(1.5D)
    };

    public TCWall(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.FENCE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(true))
                .withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false))
                .withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false)));
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        IBlockState state2 = state;
        state2 = this.getActualState(state2, source, pos);
        return AABB_BY_INDEX[getAABBindex(state2)];
    }

    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn,
            final BlockPos pos, final AxisAlignedBB entityBox,
            final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn,
            final boolean isActualState) {
        if (!isActualState) {
            IBlockState state2 = state;
            state2 = this.getActualState(state2, worldIn, pos);
        }

        addCollisionBoxToList(pos, entityBox, collidingBoxes,
                CLIP_AABB_BY_INDEX[getAABBindex(state)]);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState,
            final IBlockAccess worldIn, final BlockPos pos) {
        IBlockState state = blockState;
        state = this.getActualState(state, worldIn, pos);
        return CLIP_AABB_BY_INDEX[getAABBindex(state)];
    }

    private static int getAABBindex(final IBlockState state) {
        int i = 0;

        if (state.getValue(NORTH).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (state.getValue(EAST).booleanValue()) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (state.getValue(SOUTH).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (state.getValue(WEST).booleanValue()) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    @Override
    public boolean canConnectTo(final IBlockAccess worldIn, final BlockPos pos,
            final EnumFacing facing) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, pos, facing);
        final boolean flag = blockfaceshape.equals(BlockFaceShape.MIDDLE_POLE_THICK)
                || blockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;
        return blockfaceshape == BlockFaceShape.SOLID || flag;
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        final boolean flag = canWallConnectTo(worldIn, pos, EnumFacing.NORTH);
        final boolean flag1 = canWallConnectTo(worldIn, pos, EnumFacing.EAST);
        final boolean flag2 = canWallConnectTo(worldIn, pos, EnumFacing.SOUTH);
        final boolean flag3 = canWallConnectTo(worldIn, pos, EnumFacing.WEST);
        final boolean flagNS = flag && flag2;
        final boolean flagNNS = !flag && !flag2;
        final boolean flagEW = flag1 && flag3;
        final boolean flagNEW = !flag1 && !flag3;
        final boolean flag4 = flagNS && flagNEW || flagNNS && flagEW;
        return state.withProperty(UP, Boolean.valueOf(!flag4 || !worldIn.isAirBlock(pos.up())))
                .withProperty(NORTH, Boolean.valueOf(flag))
                .withProperty(EAST, Boolean.valueOf(flag1))
                .withProperty(SOUTH, Boolean.valueOf(flag2))
                .withProperty(WEST, Boolean.valueOf(flag3));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                UP, NORTH, EAST, WEST, SOUTH
        });
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return !(face.equals(EnumFacing.UP) || face.equals(EnumFacing.DOWN))
                ? BlockFaceShape.MIDDLE_POLE_THICK
                : BlockFaceShape.CENTER_BIG;
    }

    @Override
    public boolean canBeConnectedTo(final IBlockAccess world, final BlockPos pos,
            final EnumFacing facing) {
        return canConnectTo(world, pos.offset(facing), facing.getOpposite());
    }

    private boolean canWallConnectTo(final IBlockAccess world, final BlockPos pos,
            final EnumFacing facing) {
        final BlockPos other = pos.offset(facing);
        final Block block = world.getBlockState(other).getBlock();
        return block.canBeConnectedTo(world, other, facing.getOpposite())
                || canConnectTo(world, other, facing.getOpposite());
    }
}