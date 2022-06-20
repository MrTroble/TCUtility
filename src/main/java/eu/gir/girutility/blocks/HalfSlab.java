package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.material.Material;

public class HalfSlab extends SlabBase {
    
    public HalfSlab(Material material) {
        super(material);
        setCreativeTab(GIRTabs.tab);
    }
    
    @Override
    public boolean isDouble() {
        return false;
    }
}
