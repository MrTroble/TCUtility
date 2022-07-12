package eu.gir.girutility.items;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;

public class SlabItem extends ItemSlab {

    public SlabItem(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
        super(block, singleSlab, doubleSlab);
        setCreativeTab(GIRTabs.tab);
    }

}
