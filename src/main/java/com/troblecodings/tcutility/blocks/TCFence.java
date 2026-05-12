package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.FenceBlock;

public class TCFence extends FenceBlock {

    public TCFence(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }
}
