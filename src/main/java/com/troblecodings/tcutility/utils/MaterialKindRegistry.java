package com.troblecodings.tcutility.utils;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.world.level.block.Block;

/**
 * Block -> {@link MaterialKind}-Lookup. 1.20 entfernt die Material-Klasse, deren Identitaet
 * frueher in {@code state.getMaterial()} verfuegbar war. Wir merken uns die JSON-deklarierte
 * Kategorie pro Block-Instanz und fragen sie ueber diesen statischen Index ab -- Eintraege
 * werden waehrend des BLOCKS-RegisterEvent in {@code TCBlocks.onRegister} gesetzt.
 */
public final class MaterialKindRegistry {

    private static final Map<Block, MaterialKind> KIND_BY_BLOCK = new IdentityHashMap<>();

    private MaterialKindRegistry() {
    }

    public static void put(final Block block, final MaterialKind kind) {
        KIND_BY_BLOCK.put(block, kind);
    }

    public static MaterialKind get(final Block block) {
        return KIND_BY_BLOCK.getOrDefault(block, MaterialKind.WOOD);
    }
}
