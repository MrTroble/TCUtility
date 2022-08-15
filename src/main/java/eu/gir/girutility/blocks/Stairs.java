package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class Stairs extends BlockStairs {

    public Stairs(IBlockState modelState, BlockCreateInfo blockInfo) {
        super(setBlockState());
        setCreativeTab(GIRTabs.tab);
        setHardness(blockInfo.hardness);
        setSoundType(blockInfo.soundtype);
        setLightOpacity(blockInfo.opacity);
        this.useNeighborBrightness = true;
    }

    public static IBlockState setBlockState() {
        String material = "none";
        for (String materials : material) {
            switch (materials) {
                case "GLASS":
                    return (IBlockState) getBlockById(20);
                    break;     
                default:
                    break;
            }
        }
        return (IBlockState) getBlockById(0);
    }
}
