package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.LadderBlock;

public class TCLadder extends LadderBlock {

    public TCLadder(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
    }
}
