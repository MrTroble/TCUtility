package com.troblecodings.tcutility.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCStairs extends TCCube {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumHalf> HALF =
            PropertyEnum.<EnumHalf>create("half", EnumHalf.class);
    public static final PropertyEnum<EnumShape> SHAPE =
            PropertyEnum.<EnumShape>create("shape", EnumShape.class);

    protected static final AxisAlignedBB AABB_SLAB_TOP =
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST =
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST =
            new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH =
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH =
            new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NW =
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NE =
            new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SW =
            new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SE =
            new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST =
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH =
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NW =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NE =
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SW =
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SE =
            new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);

    public TCStairs(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(HALF, EnumHalf.BOTTOM).withProperty(SHAPE, EnumShape.STRAIGHT));
        setCreativeTab(TCTabs.STAIRS);
        this.useNeighborBrightness = true;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS))
            return BlockRenderLayer.TRANSLUCENT;
        else if (this.getMaterial(getDefaultState()).equals(Material.ANVIL))
            return BlockRenderLayer.CUTOUT_MIPPED;
        return BlockRenderLayer.CUTOUT;
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

        for (final AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    private static List<AxisAlignedBB> getCollisionBoxList(final IBlockState bstate) {
        final List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        final boolean flag = bstate.getValue(HALF).equals(EnumHalf.TOP);
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        final EnumShape blockShape = bstate.getValue(SHAPE);

        if (blockShape.equals(EnumShape.STRAIGHT) || blockShape.equals(EnumShape.INNER_LEFT)
                || blockShape.equals(EnumShape.INNER_RIGHT)) {
            list.add(getCollQuarterBlock(bstate));
        }

        if (!blockShape.equals(EnumShape.STRAIGHT)) {
            list.add(getCollEighthBlock(bstate));
        }

        return list;
    }

    private static AxisAlignedBB getCollQuarterBlock(final IBlockState bstate) {
        final boolean flag = bstate.getValue(HALF).equals(EnumHalf.TOP);

        switch (bstate.getValue(FACING)) {
            case NORTH:
            default:
                return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
            case SOUTH:
                return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
            case WEST:
                return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
            case EAST:
                return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
        }
    }

    private static AxisAlignedBB getCollEighthBlock(final IBlockState bstate) {
        final EnumFacing enumfacing = bstate.getValue(FACING);
        EnumFacing enumfacing1;

        switch (bstate.getValue(SHAPE)) {
            case OUTER_LEFT:
            default:
                enumfacing1 = enumfacing;
                break;
            case OUTER_RIGHT:
                enumfacing1 = enumfacing.rotateY();
                break;
            case INNER_RIGHT:
                enumfacing1 = enumfacing.getOpposite();
                break;
            case INNER_LEFT:
                enumfacing1 = enumfacing.rotateYCCW();
        }

        final boolean flag = bstate.getValue(HALF).equals(EnumHalf.TOP);

        switch (enumfacing1) {
            case NORTH:
            default:
                return flag ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
            case SOUTH:
                return flag ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
            case WEST:
                return flag ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
            case EAST:
                return flag ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        IBlockState state2 = state;
        state2 = this.getActualState(state2, worldIn, pos);

        if (face.getAxis().equals(EnumFacing.Axis.Y))
            return face == EnumFacing.UP == (state2.getValue(HALF) == EnumHalf.TOP)
                    ? BlockFaceShape.SOLID
                    : BlockFaceShape.UNDEFINED;
        else {
            final EnumShape stairShape = state2.getValue(SHAPE);

            if (!(stairShape.equals(EnumShape.OUTER_LEFT)
                    || stairShape.equals(EnumShape.OUTER_RIGHT))) {
                final EnumFacing enumfacing = state2.getValue(FACING);

                switch (stairShape) {
                    case INNER_RIGHT:
                        return !(enumfacing.equals(face) || enumfacing.equals(face.rotateYCCW()))
                                ? BlockFaceShape.UNDEFINED
                                : BlockFaceShape.SOLID;
                    case INNER_LEFT:
                        return !(enumfacing.equals(face) || enumfacing.equals(face.rotateY()))
                                ? BlockFaceShape.UNDEFINED
                                : BlockFaceShape.SOLID;
                    case STRAIGHT:
                        return enumfacing.equals(face) ? BlockFaceShape.SOLID
                                : BlockFaceShape.UNDEFINED;
                    default:
                        return BlockFaceShape.UNDEFINED;
                }
            } else
                return BlockFaceShape.UNDEFINED;
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
    public boolean isTopSolid(final IBlockState state) {
        return state.getValue(HALF).equals(EnumHalf.TOP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate =
                super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(SHAPE, EnumShape.STRAIGHT);
        return !facing.equals(EnumFacing.DOWN) && (facing.equals(EnumFacing.UP) || hitY <= 0.5D)
                ? iblockstate.withProperty(HALF, EnumHalf.BOTTOM)
                : iblockstate.withProperty(HALF, EnumHalf.TOP);
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(final IBlockState blockState, final World worldIn,
            final BlockPos pos, final Vec3d start, final Vec3d end) {
        final List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (final AxisAlignedBB axisalignedbb : getCollisionBoxList(
                this.getActualState(blockState, worldIn, pos))) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (final RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                final double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1) {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(HALF,
                (meta & 4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
        return iblockstate;
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;

        if (state.getValue(HALF).equals(EnumHalf.TOP)) {
            i |= 4;
        }

        i = i | 5 - state.getValue(FACING).getIndex();
        return i;
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        return state.withProperty(SHAPE, getStairsShape(state, worldIn, pos));
    }

    private static EnumShape getStairsShape(final IBlockState state, final IBlockAccess worldIn,
            final BlockPos blockPos) {
        final EnumFacing enumfacing = state.getValue(FACING);
        final IBlockState iblockstate = worldIn.getBlockState(blockPos.offset(enumfacing));

        if (isBlockStairs(iblockstate) && state.getValue(HALF).equals(iblockstate.getValue(HALF))) {
            final EnumFacing enumfacing1 = iblockstate.getValue(FACING);

            if (!enumfacing1.getAxis().equals(state.getValue(FACING).getAxis())
                    && isDifferentStairs(state, worldIn, blockPos, enumfacing1.getOpposite())) {
                if (enumfacing1.equals(enumfacing.rotateYCCW()))
                    return EnumShape.OUTER_LEFT;

                return EnumShape.OUTER_RIGHT;
            }
        }

        final IBlockState iblockstate1 =
                worldIn.getBlockState(blockPos.offset(enumfacing.getOpposite()));

        if (isBlockStairs(iblockstate1)
                && state.getValue(HALF).equals(iblockstate1.getValue(HALF))) {
            final EnumFacing enumfacing2 = iblockstate1.getValue(FACING);

            if (!enumfacing2.getAxis().equals(state.getValue(FACING).getAxis())
                    && isDifferentStairs(state, worldIn, blockPos, enumfacing2)) {
                if (enumfacing2.equals(enumfacing.rotateYCCW()))
                    return EnumShape.INNER_LEFT;

                return EnumShape.INNER_RIGHT;
            }
        }

        return EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(final IBlockState state, final IBlockAccess worldIn,
            final BlockPos blockPos, final EnumFacing facing) {
        final IBlockState iblockstate = worldIn.getBlockState(blockPos.offset(facing));
        return !(isBlockStairs(iblockstate)
                && iblockstate.getValue(FACING).equals(state.getValue(FACING))
                && iblockstate.getValue(HALF).equals(state.getValue(HALF)));
    }

    public static boolean isBlockStairs(final IBlockState state) {
        return state.getBlock() instanceof TCStairs;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        final EnumFacing enumfacing = state.getValue(FACING);
        final EnumShape enumShape = state.getValue(SHAPE);

        switch (mirrorIn) {
            case LEFT_RIGHT:

                if (enumfacing.getAxis().equals(EnumFacing.Axis.Z)) {
                    switch (enumShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.INNER_RIGHT);
                        case STRAIGHT:
                        default:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }

                break;
            case FRONT_BACK:

                if (enumfacing.getAxis().equals(EnumFacing.Axis.X)) {
                    switch (enumShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE,
                                    EnumShape.INNER_LEFT);
                        case STRAIGHT:
                        default:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }
            default:
        }
        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FACING, HALF, SHAPE
        });
    }
}
