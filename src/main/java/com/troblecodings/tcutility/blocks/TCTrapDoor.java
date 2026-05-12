package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.TrapDoorBlock;

public class TCTrapDoor extends TrapDoorBlock {

    public TCTrapDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }
}
