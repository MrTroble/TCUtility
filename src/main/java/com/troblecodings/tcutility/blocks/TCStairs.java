package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.MaterialKind;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;

public class TCStairs extends StairBlock {

    public TCStairs(final BlockCreateInfo blockInfo) {
        super(stateForKind(blockInfo.kind), blockInfo.toNonSolidProperties());
    }

    /**
     * StairBlock erwartet einen Parent-Block-State, dessen Pickaxe-/Sound-/Drop-Verhalten die
     * Treppe spiegeln soll. Aus dem JSON-Material wird ein passender Vanilla-Block abgeleitet;
     * Default fuer unbekannte Materialien bleibt Eichenbretter.
     */
    private static BlockState stateForKind(final MaterialKind kind) {
        switch (kind) {
            case METAL:
                return Blocks.IRON_BLOCK.defaultBlockState();
            case STONE:
                return Blocks.STONE.defaultBlockState();
            case GLASS:
                return Blocks.GLASS.defaultBlockState();
            case SAND:
                return Blocks.SAND.defaultBlockState();
            case CLAY:
                return Blocks.CLAY.defaultBlockState();
            case SNOW:
                return Blocks.SNOW_BLOCK.defaultBlockState();
            case ICE:
            case ICE_SOLID:
                return Blocks.ICE.defaultBlockState();
            case WOOL:
                return Blocks.WHITE_WOOL.defaultBlockState();
            case DIRT:
                return Blocks.DIRT.defaultBlockState();
            case GRASS:
                return Blocks.GRASS_BLOCK.defaultBlockState();
            case HEAVY_METAL:
                return Blocks.ANVIL.defaultBlockState();
            default:
                return Blocks.OAK_PLANKS.defaultBlockState();
        }
    }
}
