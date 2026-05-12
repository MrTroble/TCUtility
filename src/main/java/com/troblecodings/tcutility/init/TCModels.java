package com.troblecodings.tcutility.init;

import java.util.List;
import java.util.Set;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.utils.MaterialKind;
import com.troblecodings.tcutility.utils.MaterialKindRegistry;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Client-seitige ColorHandler-Registrierung fuer alle Bloecke, deren JSON-Material auf "grass"
 * (intern {@link MaterialKind#GRASS}) zeigt: ohne den dynamischen Tint waeren gefaerbte
 * Grass-Bloecke biome-unabhaengig knallgruen statt biome-spezifisch eingefaerbt. 1.20 entfernt
 * vanilla {@code Material}, daher wird der Vergleich auf den mod-eigenen MaterialKind verlagert.
 *
 * <p>26.1.2 / MC 1.22: Der Color-Handler ist jetzt ein {@link BlockTintSource} und wird ueber
 * {@code RegisterColorHandlersEvent.BlockTintSources} mit einer {@code List<BlockTintSource>}
 * pro Block registriert (statt der alten {@code BlockColor}-Lambda).
 */
@EventBusSubscriber(modid = TCUtilityMain.MODID, value = Dist.CLIENT)
public final class TCModels {

    private TCModels() {
    }

    private static final BlockTintSource GRASS_TINT = new BlockTintSource() {
        @Override
        public int color(final BlockState state) {
            return 0xFF00FF00;
        }

        @Override
        public int colorInWorld(final BlockState state, final BlockAndTintGetter world,
                final BlockPos pos) {
            return BiomeColors.getAverageGrassColor(world, pos);
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of();
        }
    };

    @SubscribeEvent
    public static void registerBlockColor(final RegisterColorHandlersEvent.BlockTintSources event) {
        TCBlocks.blocksToRegister.forEach(block -> {
            if (MaterialKindRegistry.get(block) == MaterialKind.GRASS) {
                event.getBlockColors().register(List.of(GRASS_TINT), block);
            }
        });
    }

    // 1.21.4: RegisterColorHandlersEvent.Item ist entfernt -- Item-Tints laufen jetzt rein
    // ueber JSON-{@code ItemTintSources} (data-driven). Der dynamische Grass-Item-Tint laesst
    // sich damit nicht mehr API-seitig hinkleben; Inventarbild der Grass-Variante zeigt damit
    // den statischen Texture-Wert ohne Biome-Faerbung. Zugehoeriger Welt-Tint laeuft weiter
    // ueber den Block-ColorHandler, daher wird der platzierte Block korrekt biome-eingefaerbt.
}
