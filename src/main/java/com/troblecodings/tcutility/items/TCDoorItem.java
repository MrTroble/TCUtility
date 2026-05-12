package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DoubleHighBlockItem;

public class TCDoorItem extends DoubleHighBlockItem {

    public TCDoorItem(final Block block) {
        super(block, new Item.Properties().tab(TCTabs.DOORS));
        if (block instanceof TCDoor) {
            ((TCDoor) block).setItem(this);
        }
    }
}
