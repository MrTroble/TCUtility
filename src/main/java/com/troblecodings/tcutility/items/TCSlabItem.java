package com.troblecodings.tcutility.items;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

/**
 * 1.14.4: SlabBlock kennt seinen Double-State selber, daher entfaellt die
 * onItemUse-Sonderlogik aus 1.12.2.
 */
public class TCSlabItem extends BlockItem {

    public TCSlabItem(final Block block) {
        super(block, new Item.Properties());
    }
}
