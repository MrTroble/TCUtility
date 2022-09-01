package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks.EnumType;

public class FenceGate extends BlockFenceGate {

    public FenceGate(final EnumType woodType) {
        super(woodType);
        setCreativeTab(GIRTabs.tab);
    }
}
