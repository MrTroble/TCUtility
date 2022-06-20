package eu.gir.girutility.blocks;

import net.minecraft.block.material.Material;

public class DoubleSlab extends SlabBase {
    
    public DoubleSlab(Material material) {
        super(material);
    }
    
    @Override
    public boolean isDouble() {
        return true;
    }

}
