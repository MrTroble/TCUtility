package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class Fence extends BlockFence {

    public Fence(final Material materialIn, final MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setCreativeTab(TCTabs.FENCE);
    }
}