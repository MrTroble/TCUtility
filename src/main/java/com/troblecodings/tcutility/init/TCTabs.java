package com.troblecodings.tcutility.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class TCTabs {

    private TCTabs() {
    }

    public static final CreativeTabs SPECIAL = new CreativeTabs("tcspecial") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.GLASS_PANE);
        }
    };

    public static final CreativeTabs BLOCKS = new CreativeTabs("tcblocks") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.PLANKS);
        }
    };

    public static final CreativeTabs SLABS = new CreativeTabs("tcslabs") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.WOODEN_SLAB);
        }
    };

    public static final CreativeTabs STAIRS = new CreativeTabs("tcstairs") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.OAK_STAIRS);
        }
    };

    public static final CreativeTabs FENCE = new CreativeTabs("tcfence") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.OAK_FENCE);
        }
    };

    public static final CreativeTabs DOORS = new CreativeTabs("tcdoors") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.OAK_DOOR);
        }
    };

}
