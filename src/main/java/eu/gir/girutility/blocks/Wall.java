package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;

public class Wall extends BlockWall {

    public Wall(final BlockCreateInfo blockInfo) {
        super(new Block(blockInfo.material));
        setCreativeTab(GIRTabs.tab);
        setHardness(blockInfo.hardness);
        setSoundType(blockInfo.soundtype);
        setLightOpacity(blockInfo.opacity);
    }
}