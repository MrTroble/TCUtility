package com.troblecodings.tcutility.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.FALSE)
                .withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE)
                .withProperty(WEST, Boolean.FALSE).withProperty(PANE_NORTH, Boolean.FALSE)
                .withProperty(PANE_EAST, Boolean.FALSE).withProperty(PANE_SOUTH, Boolean.FALSE)
                .withProperty(PANE_WEST, Boolean.FALSE).withProperty(PANE_UP, Boolean.FALSE)
                .withProperty(PANE_DOWN, Boolean.FALSE));
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos,
            final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn,
            final boolean isActualState) {
        if (!isActualState) {
            state = this.getActualState(state, worldIn, pos);
        }

        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[0]);

        if (state.getValue(NORTH).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.NORTH)]);
        }

        if (state.getValue(SOUTH).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.SOUTH)]);
        }

        if (state.getValue(EAST).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.EAST)]);
        }

        if (state.getValue(WEST).booleanValue()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.WEST)]);
        }
    }

    private static int getBoundingBoxIndex(final EnumFacing p_185729_0_) {
        return 1 << p_185729_0_.getHorizontalIndex();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = this.getActualState(state, source, pos);
        return AABB_BY_INDEX[getBoundingBoxIndex(state)];
    }

    private static int getBoundingBoxIndex(final IBlockState state) {
        int i = 0;

        if (state.getValue(NORTH).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.NORTH);
        }

        if (state.getValue(EAST).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.EAST);
        }

        if (state.getValue(SOUTH).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.SOUTH);
        }

        if (state.getValue(WEST).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.WEST);
        }

        return i;
    }

    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return super.getItemDropped(state, rand, fortune);
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess,
            final BlockPos pos, final EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false
                : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public final boolean attachesTo(final IBlockAccess p_193393_1_, final IBlockState state, final BlockPos pos,
            final EnumFacing facing) {
        final Block block = state.getBlock();
        final BlockFaceShape blockfaceshape = state.getBlockFaceShape(p_193393_1_, pos, facing);
        return !isExcepBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID
                || blockfaceshape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    protected static boolean isExcepBlockForAttachWithPiston(final Block p_193394_0_) {
        return p_193394_0_ instanceof BlockShulkerBox || p_193394_0_ instanceof BlockLeaves
                || p_193394_0_ == Blocks.BEACON || p_193394_0_ == Blocks.CAULDRON
                || p_193394_0_ == Blocks.GLOWSTONE || p_193394_0_ == Blocks.ICE
                || p_193394_0_ == Blocks.SEA_LANTERN || p_193394_0_ == Blocks.PISTON
                || p_193394_0_ == Blocks.STICKY_PISTON || p_193394_0_ == Blocks.PISTON_HEAD
                || p_193394_0_ == Blocks.MELON_BLOCK || p_193394_0_ == Blocks.PUMPKIN
                || p_193394_0_ == Blocks.LIT_PUMPKIN || p_193394_0_ == Blocks.BARRIER;
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
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH))
                        .withProperty(EAST, state.getValue(WEST))
                        .withProperty(SOUTH, state.getValue(NORTH))
                        .withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST))
                        .withProperty(EAST, state.getValue(SOUTH))
                        .withProperty(SOUTH, state.getValue(WEST))
                        .withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST))
                        .withProperty(EAST, state.getValue(NORTH))
                        .withProperty(SOUTH, state.getValue(EAST))
                        .withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH,
                        state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST,
                        state.getValue(EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    @Override
    public boolean canBeConnectedTo(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {
        final BlockPos offset = pos.offset(facing);
        return attachesTo(world, world.getBlockState(offset), offset, facing.getOpposite());
    }

    public boolean canPaneConnectTo(final IBlockAccess world, final BlockPos pos, final EnumFacing dir) {
        final BlockPos other = pos.offset(dir);
        final IBlockState state = world.getBlockState(other);
        return state.getBlock().canBeConnectedTo(world, other, dir.getOpposite())
                || attachesTo(world, state, other, dir.getOpposite());
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos,
            final EnumFacing face) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN
                : BlockFaceShape.CENTER_SMALL;
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        state = state.withProperty(NORTH, this.attachesTo(worldIn,
                worldIn.getBlockState(pos.north()), pos.north(), EnumFacing.SOUTH));
        state = state.withProperty(EAST, this.attachesTo(worldIn, worldIn.getBlockState(pos.east()),
                pos.east(), EnumFacing.WEST));
        state = state.withProperty(SOUTH, this.attachesTo(worldIn,
                worldIn.getBlockState(pos.south()), pos.south(), EnumFacing.NORTH));
        state = state.withProperty(WEST, this.attachesTo(worldIn, worldIn.getBlockState(pos.west()),
                pos.west(), EnumFacing.EAST));
        state = state.withProperty(PANE_NORTH, this.attachesToBlock(worldIn, pos.north()));
        state = state.withProperty(PANE_EAST, this.attachesToBlock(worldIn, pos.east()));
        state = state.withProperty(PANE_SOUTH, this.attachesToBlock(worldIn, pos.south()));
        state = state.withProperty(PANE_WEST, this.attachesToBlock(worldIn, pos.west()));
        state = state.withProperty(PANE_UP, this.attachesToBlock(worldIn, pos.up()));
        state = state.withProperty(PANE_DOWN, this.attachesToBlock(worldIn, pos.down()));
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, PANE_NORTH, PANE_EAST,
                PANE_SOUTH, PANE_WEST, PANE_UP, PANE_DOWN);
    }

    private boolean attachesToBlock(final IBlockAccess world, final BlockPos pos) {
        final Block block = world.getBlockState(pos).getBlock();
        return block instanceof BlockPane;
    }

}
