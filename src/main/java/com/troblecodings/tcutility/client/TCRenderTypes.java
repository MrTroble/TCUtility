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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-seitige Render-Layer-Registrierung. Loest die 1.14.4-Lösung ab, bei
 * der jeder Block in {@code getRenderLayer()} sein eigenes
 * {@code BlockRenderLayer} zurueckgegeben hat -- das gibt's ab 1.15 nicht
 * mehr. Stattdessen wird hier pro registriertem Block der passende
 * {@link RenderType} hinterlegt: Blocktyp + Material entscheiden gemeinsam,
 * weil eine Holztuer CUTOUT, aber eine Glastuer TRANSLUCENT braucht.
 */
@Mod.EventBusSubscriber(modid = TCUtilityMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
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

    private static RenderType layerFor(final Block block) {
        final Material mat = block.defaultBlockState().getMaterial();
        if (mat == Material.GLASS || mat == Material.ICE || mat == Material.ICE_SOLID) {
            return RenderType.translucent();
        }
        // Bloecke mit ausgefraester / nicht-rechteckiger Geometrie brauchen
        // einen CUTOUT-Layer, damit die Texturen-Alpha sauber ausgeschnitten
        // werden -- unabhaengig vom Material, das zB. bei Holztueren OPAQUE
        // ist und ohne diesen Spezialfall mit SOLID gerendert wuerde. Wir
        // zaehlen hier alle TC-Klassen auf, deren Modelle praktisch nie
        // volle Cubes sind; Pipes (TCCubeRotation) zaehlen mit dazu, weil
        // Content-Pack-Models dort haeufig schmale Geometrien definieren,
        // deren transparente Texturanteile sonst schwarz gerendert werden.
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
        if (mat == Material.LEAVES || mat == Material.PLANT) {
            return RenderType.cutoutMipped();
        }
        if (!mat.isSolidBlocking()) {
            return RenderType.cutout();
        }
        return RenderType.solid();
    }
}
