package com.troblecodings.tcutility.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TCTabs {

    public static final CreativeTabs tab = new CreativeTabs("TCUtility") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(TCBlocks.CRATE);
        }

    };

}
