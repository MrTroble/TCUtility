package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.item.Item;

public class TCDoor extends DoorBlock {

    protected Item item;

    public TCDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo.toNonSolidProperties(), BlockSetType.OAK);
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
