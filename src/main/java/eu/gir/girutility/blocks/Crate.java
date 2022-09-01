package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Crate extends Block {

    public Crate() {
        super(Material.WOOD);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
        setCreativeTab(GIRTabs.tab);
    }

}
