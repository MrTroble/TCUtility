package eu.gir.girutility.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GIRModels {

    @SubscribeEvent
    public static void register(final ModelRegistryEvent event) {
        for (int i = 0; i < GIRBlocks.blocksToRegister.size(); i++) {
            registerModel(Item.getItemFromBlock(GIRBlocks.blocksToRegister.get(i)));
        }

        for (int j = 0; j < GIRItems.itemsToRegister.size(); j++) {
            registerModel(GIRItems.itemsToRegister.get(j));
        }
    }

    private static void registerModel(final Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
