package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class Fence extends BlockFence {

    public Fence(Material materialIn, MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setCreativeTab(GIRTabs.tab);
    }

}
