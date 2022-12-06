package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class ItemSlab extends net.minecraft.item.ItemSlab {

    public ItemSlab(final Block block, final BlockSlab singleSlab, final BlockSlab doubleSlab) {
        super(block, singleSlab, doubleSlab);
        setCreativeTab(TCTabs.SLABS);
    }

}
