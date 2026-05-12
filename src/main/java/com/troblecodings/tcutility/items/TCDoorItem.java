package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCDoor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TCDoorItem extends DoubleHighBlockItem {

    public TCDoorItem(final Block block, final ResourceKey<Item> itemKey) {
        // 1.21.2+: Item.Properties verlangt setId(ResourceKey<Item>) vor dem Item-Ctor;
        // Item.<init> sucht beim DataComponents-Setup die descriptionId und NPEt sonst.
        super(block, new Item.Properties().setId(itemKey));
        if (block instanceof TCDoor) {
            ((TCDoor) block).setItem(this);
        }
    }
}
