package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.DoorBlock;
import net.minecraft.item.Item;

public class TCDoor extends DoorBlock {

    protected Item item;

    public TCDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties());
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
