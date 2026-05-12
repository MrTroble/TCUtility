package com.troblecodings.tcutility.utils;

import net.minecraft.world.level.material.MapColor;

/**
 * Mod-eigenes Pendant zur in 1.20 entfernten {@code net.minecraft.world.level.material.Material}.
 * Die JSON-{@code material}-Strings ("wood", "iron", ...) werden auf diesen Enum gemappt; daraus
 * leiten wir den BlockBehaviour-{@link MapColor} ab und treffen die mod-internen
 * Verzweigungen, die frueher per {@code state.getMaterial() == Material.X} liefen
 * (Door-Sound, Stair-Parent-Block, Render-Layer, Grass-ColorHandler).
 */
public enum MaterialKind {
    GRASS(MapColor.GRASS),
    DIRT(MapColor.DIRT),
    WOOD(MapColor.WOOD),
    STONE(MapColor.STONE),
    METAL(MapColor.METAL),
    WOOL(MapColor.WOOL),
    SAND(MapColor.SAND),
    GLASS(MapColor.NONE),
    ICE(MapColor.ICE),
    ICE_SOLID(MapColor.ICE),
    SNOW(MapColor.SNOW),
    CLAY(MapColor.CLAY),
    HEAVY_METAL(MapColor.METAL),
    LEAVES(MapColor.PLANT),
    PLANT(MapColor.PLANT),
    WATER(MapColor.WATER);

    private final MapColor mapColor;

    MaterialKind(final MapColor mapColor) {
        this.mapColor = mapColor;
    }

    public MapColor mapColor() {
        return mapColor;
    }

    public boolean isTransparent() {
        return this == GLASS || this == ICE || this == ICE_SOLID || this == LEAVES || this == PLANT;
    }
}
