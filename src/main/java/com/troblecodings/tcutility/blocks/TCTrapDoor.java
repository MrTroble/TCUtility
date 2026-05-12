package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class TCTrapDoor extends TrapDoorBlock {

    public TCTrapDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties(), BlockSetType.OAK);
    }
}
