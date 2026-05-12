package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.WallBlock;

public class TCWall extends WallBlock {

    public TCWall(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }
}
