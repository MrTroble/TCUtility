package com.troblecodings.tcutility.blocks;

import java.util.Random;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SlabBase extends BlockSlab {

    Block half;
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.<Variant>create("variant",
            Variant.class);

    public SlabBase(final BlockSlab half, final BlockCreateInfo blockInfo) {
        super(blockInfo.material);
        setCreativeTab(TCTabs.TAB);
        setHardness(blockInfo.hardness);
        setSoundType(blockInfo.soundtype);
        setLightOpacity(blockInfo.opacity);
        this.useNeighborBrightness = !this.isDouble();
        IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble())
            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
    }

    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isDouble()) {
            return new ItemStack(this);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) {
            EnumBlockHalf value = EnumBlockHalf.BOTTOM;
            if ((meta & 8) != 0)
                value = EnumBlockHalf.TOP;
            state = state.withProperty(HALF, value);
        }
        return state;
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int meta = 0;
        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (!isDouble())
            return new BlockStateContainer(this, new IProperty[] {
                    VARIANT, HALF
            });
        else
            return new BlockStateContainer(this, new IProperty[] {
                    VARIANT
            });
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(final ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    public String getUnlocalizedName(final int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    public static enum Variant implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return "default";
        }
    }

    public static class DoubleSlab extends SlabBase {
        public DoubleSlab(final BlockSlab half, final BlockCreateInfo blockInfo) {
            super(half, blockInfo);
        }

        @Override
        public boolean isDouble() {
            return true;
        }

        /*
         * @Override public ItemStack getItem(World worldIn, BlockPos pos, IBlockState
         * state) { return new ItemStack(GIRBlocks.SLAB1_HALF); }
         * 
         * @Override public Item getItemDropped(IBlockState state, Random rand, int
         * fortune) { return Item.getItemFromBlock(GIRBlocks.SLAB1_HALF); }
         */

        @Override
        public int quantityDropped(final Random random) {
            return 2;
        }

        @Override
        protected boolean canSilkHarvest() {
            return false;
        }
    }

    public static class HalfSlab extends SlabBase {

        public HalfSlab(final BlockSlab half, final BlockSlab doubleSlab,
                final BlockCreateInfo blockInfo) {
            super(half, blockInfo);
            setCreativeTab(TCTabs.TAB);
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Override
        public int quantityDropped(final Random random) {
            return this.isDouble() ? 2 : 1;
        }
    }
}