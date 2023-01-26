package com.troblecodings.tcutility.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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

public class TCWindow extends TCCube {

    public static final PropertyBool PANE_NORTH = PropertyBool.create("pane_north");
    public static final PropertyBool PANE_EAST = PropertyBool.create("pane_east");
    public static final PropertyBool PANE_SOUTH = PropertyBool.create("pane_south");
    public static final PropertyBool PANE_WEST = PropertyBool.create("pane_west");
    public static final PropertyBool PANE_UP = PropertyBool.create("pane_up");
    public static final PropertyBool PANE_DOWN = PropertyBool.create("pane_down");

    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };

    public TCWindow(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.SPECIAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PANE_NORTH, false)
                .withProperty(PANE_EAST, false).withProperty(PANE_SOUTH, false)
                .withProperty(PANE_WEST, false).withProperty(PANE_UP, false)
                .withProperty(PANE_DOWN, false));
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos,
            final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes,
            @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = this.getActualState(state, worldIn, pos);
        }

        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[0]);

        if (state.getValue(PANE_NORTH).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.NORTH)]);
        }

        if (state.getValue(PANE_SOUTH).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.SOUTH)]);
        }

        if (state.getValue(PANE_EAST).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.EAST)]);
        }

        if (state.getValue(PANE_WEST).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.WEST)]);
        }
    }

    private static int getBoundingBoxIndex(final EnumFacing facing) {
        return 1 << facing.getHorizontalIndex();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        state = this.getActualState(state, source, pos);
        return AABB_BY_INDEX[getBoundingBoxIndex(state)];
    }

    private static int getBoundingBoxIndex(final IBlockState state) {
        int i = 0;

        if (state.getValue(PANE_NORTH).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.NORTH);
        }

        if (state.getValue(PANE_EAST).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.EAST);
        }

        if (state.getValue(PANE_SOUTH).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.SOUTH);
        }

        if (state.getValue(PANE_WEST).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.WEST);
        }

        return i;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        state = state.withProperty(PANE_NORTH, this.attachesToBlock(worldIn, pos.north()));
        state = state.withProperty(PANE_EAST, this.attachesToBlock(worldIn, pos.east()));
        state = state.withProperty(PANE_SOUTH, this.attachesToBlock(worldIn, pos.south()));
        state = state.withProperty(PANE_WEST, this.attachesToBlock(worldIn, pos.west()));
        state = state.withProperty(PANE_UP, this.attachesToBlock(worldIn, pos.up()));
        state = state.withProperty(PANE_DOWN, this.attachesToBlock(worldIn, pos.down()));
        return state;
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(PANE_NORTH, state.getValue(PANE_SOUTH))
                        .withProperty(PANE_EAST, state.getValue(PANE_WEST))
                        .withProperty(PANE_SOUTH, state.getValue(PANE_NORTH))
                        .withProperty(PANE_WEST, state.getValue(PANE_EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(PANE_NORTH, state.getValue(PANE_EAST))
                        .withProperty(PANE_EAST, state.getValue(PANE_SOUTH))
                        .withProperty(PANE_SOUTH, state.getValue(PANE_WEST))
                        .withProperty(PANE_WEST, state.getValue(PANE_NORTH));
            case CLOCKWISE_90:
                return state.withProperty(PANE_NORTH, state.getValue(PANE_WEST))
                        .withProperty(PANE_EAST, state.getValue(PANE_NORTH))
                        .withProperty(PANE_SOUTH, state.getValue(PANE_EAST))
                        .withProperty(PANE_WEST, state.getValue(PANE_SOUTH));
            default:
                return state;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT:
                return state.withProperty(PANE_NORTH, state.getValue(PANE_SOUTH))
                        .withProperty(PANE_SOUTH, state.getValue(PANE_NORTH));
            case FRONT_BACK:
                return state.withProperty(PANE_EAST, state.getValue(PANE_WEST))
                        .withProperty(PANE_WEST, state.getValue(PANE_EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PANE_NORTH, PANE_EAST, PANE_SOUTH, PANE_WEST, PANE_UP,
                PANE_DOWN);
    }

    private boolean attachesToBlock(final IBlockAccess world, final BlockPos pos) {
        final Block block = world.getBlockState(pos).getBlock();
        return block instanceof TCWindow;
    }

}
