package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;

public class ItemDoor extends net.minecraft.item.ItemDoor {

    public ItemDoor(final Block block) {
        super(block);
        this.setCreativeTab(TCTabs.TAB);
    }
}
