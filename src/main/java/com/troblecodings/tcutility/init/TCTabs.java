package com.troblecodings.tcutility.init;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class TCTabs {

    private TCTabs() {
    }

    public static final ItemGroup SPECIAL = new ItemGroup("tcspecial") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.GLASS_PANE);
        }
    };

    public static final ItemGroup BLOCKS = new ItemGroup("tcblocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.OAK_PLANKS);
        }
    };

    public static final ItemGroup SLABS = new ItemGroup("tcslabs") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.OAK_SLAB);
        }
    };

    public static final ItemGroup STAIRS = new ItemGroup("tcstairs") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.OAK_STAIRS);
        }
    };

    public static final ItemGroup FENCE = new ItemGroup("tcfence") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.OAK_FENCE);
        }
    };

    public static final ItemGroup DOORS = new ItemGroup("tcdoors") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.OAK_DOOR);
        }
    };

    public static final ItemGroup ITEMS = new ItemGroup("tcitems") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.PAPER);
        }
    };
}
