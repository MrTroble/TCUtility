package com.troblecodings.tcutility.init;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.utils.MaterialKind;
import com.troblecodings.tcutility.utils.MaterialKindRegistry;

import net.minecraft.client.renderer.BiomeColors;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Client-seitige ColorHandler-Registrierung fuer alle Bloecke, deren JSON-Material auf "grass"
 * (intern {@link MaterialKind#GRASS}) zeigt: ohne den dynamischen Tint waeren gefaerbte
 * Grass-Bloecke biome-unabhaengig knallgruen statt biome-spezifisch eingefaerbt. 1.20 entfernt
 * vanilla {@code Material}, daher wird der Vergleich auf den mod-eigenen MaterialKind verlagert.
 */
@EventBusSubscriber(modid = TCUtilityMain.MODID, value = Dist.CLIENT)
public final class TCModels {

    private TCModels() {
    }

    @SubscribeEvent
    public static void registerBlockColor(final RegisterColorHandlersEvent.Block event) {
        TCBlocks.blocksToRegister.forEach(block -> {
            if (MaterialKindRegistry.get(block) == MaterialKind.GRASS) {
                event.getBlockColors().register((state, worldIn, pos, tintIndex) -> {
                    if (worldIn == null || pos == null) {
                        return 0xFF00FF00;
                    }
                    return BiomeColors.getAverageGrassColor(worldIn, pos);
                }, block);
            }
        });
    }

    @SubscribeEvent
    public static void registerItemColor(final RegisterColorHandlersEvent.Item event) {
        TCBlocks.blocksToRegister.forEach(block -> {
            if (MaterialKindRegistry.get(block) == MaterialKind.GRASS) {
                event.getItemColors().register((stack, tintIndex) -> 0xFF5E7A39,
                        block.asItem());
            }
        });
    }
}
