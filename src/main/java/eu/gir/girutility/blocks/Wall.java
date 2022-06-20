package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;

public class Wall extends BlockWall {
    
    public Wall(Material material) {
        super(new Block(material));
        setCreativeTab(GIRTabs.tab);
    }

}
