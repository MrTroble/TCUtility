package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.FenceGateBlock;

public class TCFenceGate extends FenceGateBlock {

    public TCFenceGate(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }
}
