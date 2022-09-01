package eu.gir.girutility.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class GIRTabs {

    public static final CreativeTabs tab = new CreativeTabs("GIRUtility") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(GIRBlocks.CRATE);
        }

    };

}
