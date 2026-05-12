package com.troblecodings.tcutility.items;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 1.14.4: SlabBlock kennt seinen Double-State selber, daher entfaellt die
 * onItemUse-Sonderlogik aus 1.12.2.
 *
 * <p>1.21.2+: Item.Properties verlangt setId vor dem BlockItem-Ctor.
 */
public class TCSlabItem extends BlockItem {

    public TCSlabItem(final Block block, final ResourceKey<Item> itemKey) {
        super(block, new Item.Properties().setId(itemKey));
    }
}
