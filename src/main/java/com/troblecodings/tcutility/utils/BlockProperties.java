package com.troblecodings.tcutility.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockProperties {

    private float hardness;
    private String material = "";
    private String soundtype = "";
    private int opacity;
    private int lightValue;
    private List<String> states;
    private List<Integer> hitbox = Arrays.asList(0, 0, 0, 16, 16, 16);
    private boolean fullblock = true;

    private static final HashMap<String, Material> MATERIAL_TABLE = translateTableMaterial();
    private static final HashMap<String, SoundType> SOUND_TABLE = translateTableSoundType();

    public static HashMap<String, Material> translateTableMaterial() {
        // JSON-facing keys keep their 1.12.2 names so existing content packs
        // continue to work; values point at the renamed 1.14.4 constants.
        final HashMap<String, Material> translateTable = new HashMap<>();
        translateTable.put("grass", Material.ORGANIC);
        translateTable.put("ground", Material.EARTH);
        translateTable.put("wood", Material.WOOD);
        translateTable.put("rock", Material.ROCK);
        translateTable.put("iron", Material.IRON);
        translateTable.put("cloth", Material.WOOL);
        translateTable.put("sand", Material.SAND);
        translateTable.put("glass", Material.GLASS);
        translateTable.put("ice", Material.ICE);
        translateTable.put("packed_ice", Material.PACKED_ICE);
        translateTable.put("snow", Material.SNOW);
        translateTable.put("clay", Material.CLAY);
        translateTable.put("anvil", Material.ANVIL);
        return translateTable;
    }

    public static HashMap<String, SoundType> translateTableSoundType() {
        final HashMap<String, SoundType> translateTable = new HashMap<>();
        translateTable.put("wood", SoundType.WOOD);
        translateTable.put("ground", SoundType.GROUND);
        translateTable.put("stone", SoundType.STONE);
        translateTable.put("metal", SoundType.METAL);
        translateTable.put("glass", SoundType.GLASS);
        translateTable.put("cloth", SoundType.CLOTH);
        translateTable.put("sand", SoundType.SAND);
        translateTable.put("snow", SoundType.SNOW);
        translateTable.put("ladder", SoundType.LADDER);
        translateTable.put("slime", SoundType.SLIME);
        translateTable.put("plant", SoundType.PLANT);
        return translateTable;
    }

    public BlockCreateInfo getBlockInfo() {
        final Material mat = MATERIAL_TABLE.get(material.toLowerCase());
        final SoundType sound = SOUND_TABLE.get(soundtype.toLowerCase());
        if (mat == null) {
            throw new IllegalStateException("The given material " + material + " is not valid.");
        }
        if (sound == null) {
            throw new IllegalStateException("The given sound type " + soundtype + " is not valid.");
        }
        return new BlockCreateInfo(mat, hardness, sound, opacity, lightValue, hitbox, fullblock);
    }

    public List<String> getStates() {
        return states;
    }
}
