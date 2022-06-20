package eu.gir.girutility.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;

public class DoubleSlab extends SlabBase {
    
    public DoubleSlab(Material material, BlockSlab half) {
        super(material, half);
    }
    
    @Override
    public boolean isDouble() {
        return true;
    }

}
