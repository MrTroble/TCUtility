package com.troblecodings.tcutility.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.world.level.block.SoundType;

public class BlockProperties {

    private float hardness;
    private String material = "";
    private String soundtype = "";
    private int opacity;
    private int lightValue;
    private List<String> states;
    private List<Integer> hitbox = Arrays.asList(0, 0, 0, 16, 16, 16);
    private boolean fullblock = true;

    private static final HashMap<String, MaterialKind> MATERIAL_TABLE = translateTableMaterial();
    private static final HashMap<String, SoundType> SOUND_TABLE = translateTableSoundType();

    public static HashMap<String, MaterialKind> translateTableMaterial() {
        // JSON-facing keys behalten ihre 1.12.2-Namen; Werte zeigen auf den 1.20-Ersatz
        // (mod-internes MaterialKind-Enum, weil vanilla Material seit 1.20 entfernt ist).
        final HashMap<String, MaterialKind> translateTable = new HashMap<>();
        translateTable.put("grass", MaterialKind.GRASS);
        translateTable.put("ground", MaterialKind.DIRT);
        translateTable.put("wood", MaterialKind.WOOD);
        translateTable.put("rock", MaterialKind.STONE);
        translateTable.put("iron", MaterialKind.METAL);
        translateTable.put("cloth", MaterialKind.WOOL);
        translateTable.put("sand", MaterialKind.SAND);
        translateTable.put("glass", MaterialKind.GLASS);
        translateTable.put("ice", MaterialKind.ICE);
        translateTable.put("packed_ice", MaterialKind.ICE_SOLID);
        translateTable.put("snow", MaterialKind.SNOW);
        translateTable.put("clay", MaterialKind.CLAY);
        translateTable.put("anvil", MaterialKind.HEAVY_METAL);
        return translateTable;
    }

    public static HashMap<String, SoundType> translateTableSoundType() {
        final HashMap<String, SoundType> translateTable = new HashMap<>();
        translateTable.put("wood", SoundType.WOOD);
        translateTable.put("ground", SoundType.GRAVEL);
        translateTable.put("stone", SoundType.STONE);
        translateTable.put("metal", SoundType.METAL);
        translateTable.put("glass", SoundType.GLASS);
        translateTable.put("cloth", SoundType.WOOL);
        translateTable.put("sand", SoundType.SAND);
        translateTable.put("snow", SoundType.SNOW);
        translateTable.put("ladder", SoundType.LADDER);
        translateTable.put("slime", SoundType.SLIME_BLOCK);
        translateTable.put("plant", SoundType.GRASS);
        return translateTable;
    }

    public BlockCreateInfo getBlockInfo() {
        final MaterialKind kind = MATERIAL_TABLE.get(material.toLowerCase());
        final SoundType sound = SOUND_TABLE.get(soundtype.toLowerCase());
        if (kind == null) {
            throw new IllegalStateException("The given material " + material + " is not valid.");
        }
        if (sound == null) {
            throw new IllegalStateException("The given sound type " + soundtype + " is not valid.");
        }
        return new BlockCreateInfo(kind, hardness, sound, opacity, lightValue, hitbox, fullblock);
    }

    public List<String> getStates() {
        return states;
    }
}
