package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;

public class TCDoorItem extends ItemDoor {

    public TCDoorItem(final Block block) {
        super(block);
        setCreativeTab(TCTabs.DOORS);
        ((TCDoor) block).setItem(this);
    }

}
