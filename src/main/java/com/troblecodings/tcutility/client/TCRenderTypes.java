package com.troblecodings.tcutility.client;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.blocks.TCBigDoor;
import com.troblecodings.tcutility.blocks.TCCubeRotation;
import com.troblecodings.tcutility.blocks.TCCubeRotationAll;
import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.blocks.TCFence;
import com.troblecodings.tcutility.blocks.TCFenceGate;
import com.troblecodings.tcutility.blocks.TCGarageDoor;
import com.troblecodings.tcutility.blocks.TCGarageGate;
import com.troblecodings.tcutility.blocks.TCHanging;
import com.troblecodings.tcutility.blocks.TCLadder;
import com.troblecodings.tcutility.blocks.TCSlab;
import com.troblecodings.tcutility.blocks.TCStairs;
import com.troblecodings.tcutility.blocks.TCTrapDoor;
import com.troblecodings.tcutility.blocks.TCWall;
import com.troblecodings.tcutility.blocks.TCWindow;
import com.troblecodings.tcutility.init.TCBlocks;
import com.troblecodings.tcutility.init.TCFluidsInit;
import com.troblecodings.tcutility.utils.MaterialKind;
import com.troblecodings.tcutility.utils.MaterialKindRegistry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * Client-seitige Render-Layer-Registrierung. Loest die 1.14.4-Lösung ab, bei der jeder Block in
 * {@code getRenderLayer()} sein eigenes {@code BlockRenderLayer} zurueckgegeben hat -- das gibt's
 * ab 1.15 nicht mehr. Stattdessen wird hier pro registriertem Block der passende {@link RenderType}
 * hinterlegt: Blocktyp + MaterialKind entscheiden gemeinsam, weil eine Holztuer CUTOUT, aber eine
 * Glastuer TRANSLUCENT braucht. 1.20: vanilla {@code Material} ist entfernt; wir lesen den
 * mod-internen MaterialKind aus {@link MaterialKindRegistry}.
 */
@EventBusSubscriber(modid = TCUtilityMain.MODID, value = Dist.CLIENT)
public final class TCRenderTypes {

    private TCRenderTypes() {
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        for (final Block block : TCBlocks.blocksToRegister) {
            ItemBlockRenderTypes.setRenderLayer(block, layerFor(block));
        }
        for (final Block fluidBlock : TCFluidsInit.blocksToRegister) {
            ItemBlockRenderTypes.setRenderLayer(fluidBlock, RenderType.translucent());
        }
    }

    /**
     * 1.21: {@code FluidType#initializeClient} ist deprecated. Texturen werden jetzt ueber
     * {@link RegisterClientExtensionsEvent#registerFluidType} pro FluidType registriert.
     * Wir leiten Still/Flow-Pfade aus dem JSON-Namen ab (assets/&lt;modid&gt;/textures/blocks/&lt;name&gt;_still.png).
     */
    @SubscribeEvent
    public static void registerFluidExtensions(final RegisterClientExtensionsEvent event) {
        for (final TCFluidsInit.FluidEntry e : TCFluidsInit.entries) {
            final ResourceLocation still = ResourceLocation.fromNamespaceAndPath(
                    TCUtilityMain.MODID, "blocks/" + e.name + "_still");
            final ResourceLocation flow = ResourceLocation.fromNamespaceAndPath(
                    TCUtilityMain.MODID, "blocks/" + e.name + "_flow");
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return still;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flow;
                }
            }, e.typeRef.get());
        }
    }

    private static RenderType layerFor(final Block block) {
        final MaterialKind kind = MaterialKindRegistry.get(block);
        if (kind == MaterialKind.GLASS || kind == MaterialKind.ICE || kind == MaterialKind.ICE_SOLID) {
            return RenderType.translucent();
        }
        // Bloecke mit ausgefraester / nicht-rechteckiger Geometrie brauchen einen CUTOUT-Layer,
        // damit die Texturen-Alpha sauber ausgeschnitten werden -- unabhaengig vom MaterialKind,
        // das zB. bei Holztueren OPAQUE ist und ohne diesen Spezialfall mit SOLID gerendert wuerde.
        if (block instanceof TCBigDoor || block instanceof TCDoor
                || block instanceof TCTrapDoor || block instanceof TCFenceGate
                || block instanceof TCGarageDoor || block instanceof TCGarageGate
                || block instanceof TCWindow || block instanceof TCLadder
                || block instanceof TCHanging || block instanceof TCFence
                || block instanceof TCWall || block instanceof TCStairs
                || block instanceof TCSlab || block instanceof TCCubeRotation
                || block instanceof TCCubeRotationAll) {
            return RenderType.cutout();
        }
        if (kind == MaterialKind.LEAVES || kind == MaterialKind.PLANT) {
            return RenderType.cutoutMipped();
        }
        return RenderType.solid();
    }
}
