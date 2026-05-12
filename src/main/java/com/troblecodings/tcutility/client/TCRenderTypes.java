package com.troblecodings.tcutility.client;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.init.TCFluidsInit;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;

/**
 * Client-seitige Render-Setup-Hooks.
 *
 * <p>26.1.2 / MC 1.22: Mojang hat das gesamte Render-Layer-Mapping in die Blockstate-JSONs
 * verschoben ({@code "render_type": "cutout|translucent|solid|tripwire"}-Property je Variant).
 * {@code ItemBlockRenderTypes#setRenderLayer} ist deshalb komplett aus der API verschwunden;
 * der Block-Asset-Migrationschritt schreibt die Layer jetzt in die generierten Blockstates,
 * deshalb gibt es hier keinen Java-seitigen FMLClientSetupEvent-Hook mehr.
 *
 * <p>Fluid-Texturen liefen frueher ueber {@link net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions#getStillTexture()}
 * /{@code getFlowingTexture()}; diese Methoden sind in 26.1.2 weg. Stattdessen registrieren wir
 * pro Fluid ein {@link FluidModel.Unbaked} via {@link RegisterFluidModelsEvent}, das die
 * Still/Flow-{@link Material}s an die Fluid-Render-Pipeline bindet. Ohne diese Registrierung
 * faellt der Renderer auf das vanilla-Default zurueck und liefert die weiss-braune
 * Missing-Texture-Mischung.
 */
@EventBusSubscriber(modid = TCUtilityMain.MODID, value = Dist.CLIENT)
public final class TCRenderTypes {

    private TCRenderTypes() {
    }

    /**
     * 1.21.4: Der DynamicFluidContainerModel-ItemModel-Codec ist nicht mehr Teil des
     * eingebauten Default-Sets. Wir muessen ihn selbst unter dem alten ID
     * {@code neoforge:fluid_container} registrieren, sonst lehnt der Codec-Resolver die
     * generierten Bucket-Item-Models ab und der Renderer fragt das Missing-Texture-Visual ab.
     */
    @SubscribeEvent
    public static void registerItemModelCodecs(final RegisterItemModelsEvent event) {
        event.register(Identifier.fromNamespaceAndPath("neoforge", "fluid_container"),
                DynamicFluidContainerModel.Unbaked.MAP_CODEC);
    }

    /**
     * 26.1.2: Fluid-Render-Pipeline registriert Still/Flow-Texturen pro {@link FluidModel}.
     * Wir leiten Sprite-Pfade aus dem JSON-Namen ab (assets/&lt;modid&gt;/textures/blocks/&lt;name&gt;_still
     * + _flow) und binden sowohl source als auch flowing Fluid an dasselbe Model.
     */
    @SubscribeEvent
    public static void registerFluidModels(final RegisterFluidModelsEvent event) {
        for (final TCFluidsInit.FluidEntry e : TCFluidsInit.entries) {
            final Material still = new Material(Identifier.fromNamespaceAndPath(
                    TCUtilityMain.MODID, "blocks/" + e.name + "_still"));
            final Material flow = new Material(Identifier.fromNamespaceAndPath(
                    TCUtilityMain.MODID, "blocks/" + e.name + "_flow"));
            final FluidModel.Unbaked model = new FluidModel.Unbaked(still, flow, null, null);
            event.register(model, e.sourceRef.get(), e.flowingRef.get());
        }
    }
}
