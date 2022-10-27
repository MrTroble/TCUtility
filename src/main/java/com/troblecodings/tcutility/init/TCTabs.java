package com.troblecodings.tcutility.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class TCTabs {
    
    private TCTabs() {
    }

    public static final CreativeTabs TAB = new CreativeTabs("TCUtility") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(TCBlocks.CRATE);
        }

    };

}
