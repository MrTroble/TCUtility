package eu.gir.girutility.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GIRModels {

	//Absoluter chad code der alle deine Probleme löst.
	//Registriet alle Objekte in Liste blocksToRegister aus GIRBlocks. 
	//reflectiveChestplate sowie andere GIRItems sind nicht teil der Liste aus GIRBlocks, dementsprechend werden sie seperat durch die Liste itemsToRegister registiert
	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {
		for (int i = 0; i < GIRBlocks.blocksToRegister.size(); i++) {
			registerModel(Item.getItemFromBlock(GIRBlocks.blocksToRegister.get(i)));
		}
		
		for (int j = 0; j < GIRItems.itemsToRegister.size(); j++) {
			registerModel(GIRItems.itemsToRegister.get(j));
		}
	}
	
//beschissener Virgin Code den niemand mag
/*	public static void register(ModelRegistryEvent event) {
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAFFIC_CONE));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE_SPRUCE));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE_BIRCH));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE_JUNGLE));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE_ACACIA));
		registerModel(Item.getItemFromBlock(GIRBlocks.CRATE_DARK_OAK));
		registerModel(Item.getItemFromBlock(GIRBlocks.BIN));
		registerModel(Item.getItemFromBlock(GIRBlocks.WOODEN_WINDOW));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_OAK));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_SPRUCE));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_BIRCH));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_JUNGLE));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_ACACIA));
		registerModel(Item.getItemFromBlock(GIRBlocks.TRAPDOOR_DARK_OAK));
		
		registerModel(GIRItems.reflectiveChestplate);
	}
*/
	
	
	private static void registerModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
