package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Concrete extends Block {

    public Concrete(final Material materialIn) {
        super(materialIn);
        this.setCreativeTab(TCTabs.BLOCKS);
    }
}
