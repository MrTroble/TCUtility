package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;

public class TCStairs extends StairsBlock {

    public TCStairs(final BlockCreateInfo blockInfo) {
        super(() -> stateForMaterial(blockInfo.material), blockInfo.toNonSolidProperties());
    }

    /**
     * StairsBlock erwartet einen "Parent-Block-State" -- den Block, dessen
     * Pickaxe-/Sound-/Drop-Verhalten die Treppe spiegeln soll. Aus dem
     * Material aus der JSON-Definition wird ein passender Vanilla-Block
     * abgeleitet; der Default fuer unbekannte Materialien bleibt
     * Eichenbretter.
     */
    private static BlockState stateForMaterial(final Material mat) {
        if (mat == Material.IRON) {
            return Blocks.IRON_BLOCK.getDefaultState();
        }
        if (mat == Material.ROCK) {
            return Blocks.STONE.getDefaultState();
        }
        if (mat == Material.GLASS) {
            return Blocks.GLASS.getDefaultState();
        }
        if (mat == Material.SAND) {
            return Blocks.SAND.getDefaultState();
        }
        if (mat == Material.CLAY) {
            return Blocks.CLAY.getDefaultState();
        }
        if (mat == Material.SNOW || mat == Material.SNOW_BLOCK) {
            return Blocks.SNOW_BLOCK.getDefaultState();
        }
        if (mat == Material.ICE || mat == Material.PACKED_ICE) {
            return Blocks.ICE.getDefaultState();
        }
        if (mat == Material.WOOL) {
            return Blocks.WHITE_WOOL.getDefaultState();
        }
        if (mat == Material.EARTH) {
            return Blocks.DIRT.getDefaultState();
        }
        if (mat == Material.ORGANIC) {
            return Blocks.GRASS_BLOCK.getDefaultState();
        }
        if (mat == Material.ANVIL) {
            return Blocks.ANVIL.getDefaultState();
        }
        return Blocks.OAK_PLANKS.getDefaultState();
    }
}
