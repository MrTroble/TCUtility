package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;

public class Wall extends BlockWall {

    public Wall(final BlockCreateInfo blockInfo) {
        super(new Block(blockInfo.material));
        setCreativeTab(TCTabs.TAB);
        setHardness(blockInfo.hardness);
        setSoundType(blockInfo.soundtype);
        setLightOpacity(blockInfo.opacity);
    }
}