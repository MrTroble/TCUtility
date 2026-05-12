package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TCFenceGate extends FenceGateBlock {

    public TCFenceGate(final BlockCreateInfo blockInfo) {
        super(WoodType.OAK, blockInfo.toNonSolidProperties());
    }
}
