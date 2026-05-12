package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.Material;

public class TCStairs extends StairBlock {

    public TCStairs(final BlockCreateInfo blockInfo) {
        super(() -> stateForMaterial(blockInfo.material), blockInfo.toNonSolidProperties());
    }

    /**
     * StairBlock erwartet einen "Parent-Block-State" -- den Block, dessen
     * Pickaxe-/Sound-/Drop-Verhalten die Treppe spiegeln soll. Aus dem
     * Material aus der JSON-Definition wird ein passender Vanilla-Block
     * abgeleitet; der Default fuer unbekannte Materialien bleibt
     * Eichenbretter.
     */
    private static BlockState stateForMaterial(final Material mat) {
        if (mat == Material.METAL) {
            return Blocks.IRON_BLOCK.defaultBlockState();
        }
        if (mat == Material.STONE) {
            return Blocks.STONE.defaultBlockState();
        }
        if (mat == Material.GLASS) {
            return Blocks.GLASS.defaultBlockState();
        }
        if (mat == Material.SAND) {
            return Blocks.SAND.defaultBlockState();
        }
        if (mat == Material.CLAY) {
            return Blocks.CLAY.defaultBlockState();
        }
        if (mat == Material.SNOW || mat == Material.SNOW) {
            return Blocks.SNOW_BLOCK.defaultBlockState();
        }
        if (mat == Material.ICE || mat == Material.ICE_SOLID) {
            return Blocks.ICE.defaultBlockState();
        }
        if (mat == Material.WOOL) {
            return Blocks.WHITE_WOOL.defaultBlockState();
        }
        if (mat == Material.DIRT) {
            return Blocks.DIRT.defaultBlockState();
        }
        if (mat == Material.GRASS) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        if (mat == Material.HEAVY_METAL) {
            return Blocks.ANVIL.defaultBlockState();
        }
        return Blocks.OAK_PLANKS.defaultBlockState();
    }
}
