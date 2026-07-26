package com.troblecodings.tcutility.init;

import com.troblecodings.tcutility.TCUtilityMain;

import net.minecraft.block.material.Material;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 1.14.4-Port: ModelLoader.setCustomModelResourceLocation und State-Mapper-API
 * gibt es nicht mehr; Item-/Blockstate-Models werden automatisch ueber die
 * blockstate-/item-model-Jsons aufgeloest. Diese Klasse haelt nur noch die
 * Color-Handler fuer Bloecke mit Material.ORGANIC (entspricht dem alten
 * Material.GRASS aus 1.12.2).
 */
@Mod.EventBusSubscriber(modid = TCUtilityMain.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public final class TCModels {

    private TCModels() {
    }

    @SubscribeEvent
    public static void registerBlockColor(final ColorHandlerEvent.Block event) {
        TCBlocks.blocksToRegister.forEach(block -> {
            if (block.getDefaultState().getMaterial() == Material.ORGANIC) {
                event.getBlockColors().register((state, worldIn, pos, tintIndex) -> {
                    if (worldIn == null || pos == null) {
                        return 0xFF00FF00;
                    }
                    return BiomeColors.getGrassColor(worldIn, pos);
                }, block);
            }
        });
    }

    @SubscribeEvent
    public static void registerItemColor(final ColorHandlerEvent.Item event) {
        TCBlocks.blocksToRegister.forEach(block -> {
            if (block.getDefaultState().getMaterial() == Material.ORGANIC) {
                event.getItemColors().register((stack, tintIndex) -> 0xFF5E7A39,
                        block.asItem());
            }
        });
    }
}
