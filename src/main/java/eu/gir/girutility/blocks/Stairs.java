package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class Stairs extends BlockStairs {

    public Stairs(IBlockState modelState) {
        super(modelState);
        setCreativeTab(GIRTabs.tab);
        this.useNeighborBrightness = true;
    }
}
