package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.SlabBlock;

public class TCSlab extends SlabBlock {

    public TCSlab(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
    }
}
