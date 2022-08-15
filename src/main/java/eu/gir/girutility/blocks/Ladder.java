package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;

public class Ladder extends BlockLadder{

    public Ladder(Material material) {
        super();
        setCreativeTab(GIRTabs.tab);
    }
}
