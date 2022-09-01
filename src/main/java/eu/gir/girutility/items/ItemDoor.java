package eu.gir.girutility.items;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;

public class ItemDoor extends net.minecraft.item.ItemDoor {
    
    public ItemDoor(final Block block) {
        super(block);
        this.setCreativeTab(GIRTabs.tab);
    }
}
