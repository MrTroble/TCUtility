package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks.EnumType;

public class FenceGate extends BlockFenceGate {

    public FenceGate(final EnumType woodType) {
        super(woodType);
        setCreativeTab(TCTabs.FENCE);
    }
}
