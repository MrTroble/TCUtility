package com.troblecodings.tcutility.blocks;

import java.util.Random;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCSlab extends TCCube {

    private Item item;

    public static final PropertyEnum<TCSlab.SlabType> TYPE =
            PropertyEnum.<TCSlab.SlabType>create("half", TCSlab.SlabType.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF =
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public TCSlab(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        setCreativeTab(TCTabs.SLABS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, SlabType.BOTTOM));
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS))
            return BlockRenderLayer.TRANSLUCENT;
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target,
            final World world, final BlockPos pos, final EntityPlayer player) {
        return getItem(world, pos, state);
    }

    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return item;
    }

    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(item);
    }

    public IProperty<?> getVariantProperty() {
        return TYPE;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public boolean isTopSolid(final IBlockState state) {
        if (state.getValue(TYPE).equals(SlabType.BOTTOM))
            return false;
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        if (state.getValue(TYPE).equals(SlabType.DOUBLE))
            return BlockFaceShape.SOLID;
        else if (face.equals(EnumFacing.UP) && state.getValue(TYPE).equals(SlabType.TOP))
            return BlockFaceShape.SOLID;
        else
            return face.equals(EnumFacing.DOWN) && state.getValue(TYPE).equals(SlabType.BOTTOM)
                    ? BlockFaceShape.SOLID
                    : BlockFaceShape.UNDEFINED;
    }

    @Override
    public int quantityDropped(final IBlockState state, final int fortune, final Random random) {
        return state.getValue(TYPE).equals(SlabType.DOUBLE) ? 2 : 1;
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        final SlabType slabtype = state.getValue(TYPE);
        switch (slabtype) {
            case DOUBLE:
                return FULL_BLOCK_AABB;
            case TOP:
                return AABB_TOP_HALF;
            default:
                return AABB_BOTTOM_HALF;
        }
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return state.getValue(TYPE) == SlabType.DOUBLE;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return state.getValue(TYPE) == SlabType.DOUBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        final IBlockState state = worldIn.getBlockState(pos);
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == this && (state.getValue(TYPE) == SlabType.BOTTOM
                || state.getValue(TYPE) == SlabType.TOP))
            return state.withProperty(TYPE, SlabType.DOUBLE);
        else {
            final IBlockState iblockstate =
                    super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                            .withProperty(TYPE, TCSlab.SlabType.BOTTOM);
            return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)
                    ? iblockstate
                    : iblockstate.withProperty(TYPE, TCSlab.SlabType.TOP);
        }
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        final IBlockState iblockstate = this.getDefaultState();
        if (meta == 2)
            return iblockstate.withProperty(TYPE, TCSlab.SlabType.DOUBLE);
        else if (meta == 1)
            return iblockstate.withProperty(TYPE, TCSlab.SlabType.TOP);
        else
            return iblockstate.withProperty(TYPE, TCSlab.SlabType.BOTTOM);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i;
        if (state.getValue(TYPE) == TCSlab.SlabType.DOUBLE) {
            i = 2;
        } else if (state.getValue(TYPE) == TCSlab.SlabType.TOP) {
            i = 1;
        } else {
            i = 0;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                TYPE
        });
    }

    public static enum SlabType implements IStringSerializable {
        TOP("top"), BOTTOM("bottom"), DOUBLE("double");

        private final String name;

        SlabType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
