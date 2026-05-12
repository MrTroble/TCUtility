package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCDoor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DoubleHighBlockItem;

public class TCDoorItem extends DoubleHighBlockItem {

    public TCDoorItem(final Block block) {
        super(block, new Item.Properties());
        if (block instanceof TCDoor) {
            ((TCDoor) block).setItem(this);
        }
    }
}
