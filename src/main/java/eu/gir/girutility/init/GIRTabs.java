package eu.gir.girutility.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class GIRTabs {

	public static final CreativeTabs tab = new CreativeTabs("GIRUtilility") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.STONE);
		}

	};

}
