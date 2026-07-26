package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;

public class TCDoorItem extends TallBlockItem {

    public TCDoorItem(final Block block) {
        super(block, new Item.Properties().group(TCTabs.DOORS));
        if (block instanceof TCDoor) {
            ((TCDoor) block).setItem(this);
        }
    }
}
