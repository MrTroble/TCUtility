package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * 1.14.4: SlabBlock kennt seinen Double-State selber, daher entfaellt die
 * onItemUse-Sonderlogik aus 1.12.2.
 */
public class TCSlabItem extends BlockItem {

    public TCSlabItem(final Block block) {
        super(block, new Item.Properties().group(TCTabs.SLABS));
    }
}
