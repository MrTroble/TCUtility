package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.TrapDoorBlock;

public class TCTrapDoor extends TrapDoorBlock {

    public TCTrapDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }
}
