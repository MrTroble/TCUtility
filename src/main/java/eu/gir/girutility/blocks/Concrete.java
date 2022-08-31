package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Concrete extends Block {
    
    public Concrete(Material materialIn) {
        super(materialIn);
        this.setCreativeTab(GIRTabs.tab);
    }
}
