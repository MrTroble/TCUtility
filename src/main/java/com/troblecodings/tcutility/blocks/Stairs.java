package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class Stairs extends BlockStairs {

    public Stairs(final IBlockState modelState) {
        super(modelState);
        setCreativeTab(TCTabs.STAIRS);
        this.useNeighborBrightness = true;
    }
}
