package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;

public class Ladder extends BlockLadder {

    public Ladder(final Material material) {
        super();
        setCreativeTab(TCTabs.SPECIAL);
    }
}
