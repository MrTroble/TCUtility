package com.troblecodings.tcutility.utils;

import java.util.HashMap;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

public class FluidProperties {

    private int luminosity = 0;
    private int density = 1000;
    private int temperature = 295;
    private int viscosity = 1000;
    private int flowLength = 8;
    private boolean canCreateSource = false;
    private String effect = "";
    private int effectDuration = 1;
    private int effectAmplifier = 1;

    private static final HashMap<String, Effect> EFFECT_TABLE = translateTableEffect();

    public FluidCreateInfo getFluidInfo() {
        final Effect effectInstance = EFFECT_TABLE.get(effect.toLowerCase());
        return new FluidCreateInfo(luminosity, density, temperature, viscosity, flowLength,
                canCreateSource, effectInstance, effectDuration, effectAmplifier);
    }

    private static HashMap<String, Effect> translateTableEffect() {
        // JSON-facing keys keep their 1.12.2 names so existing content packs
        // continue to work; values point at the renamed 1.14.4 Effects constants.
        final HashMap<String, Effect> translateTable = new HashMap<>();
        translateTable.put("", null);
        translateTable.put("absorption", Effects.ABSORPTION);
        translateTable.put("blindness", Effects.BLINDNESS);
        translateTable.put("fire_resistance", Effects.FIRE_RESISTANCE);
        translateTable.put("glowing", Effects.GLOWING);
        translateTable.put("haste", Effects.HASTE);
        translateTable.put("health_boost", Effects.HEALTH_BOOST);
        translateTable.put("hunger", Effects.HUNGER);
        translateTable.put("instant_damage", Effects.INSTANT_DAMAGE);
        translateTable.put("instant_health", Effects.INSTANT_HEALTH);
        translateTable.put("invisibility", Effects.INVISIBILITY);
        translateTable.put("jump_boost", Effects.JUMP_BOOST);
        translateTable.put("levitation", Effects.LEVITATION);
        translateTable.put("luck", Effects.LUCK);
        translateTable.put("mining_fatigue", Effects.MINING_FATIGUE);
        translateTable.put("nausea", Effects.NAUSEA);
        translateTable.put("night_vision", Effects.NIGHT_VISION);
        translateTable.put("poison", Effects.POISON);
        translateTable.put("regeneration", Effects.REGENERATION);
        translateTable.put("resistance", Effects.RESISTANCE);
        translateTable.put("saturation", Effects.SATURATION);
        translateTable.put("slowness", Effects.SLOWNESS);
        translateTable.put("speed", Effects.SPEED);
        translateTable.put("strength", Effects.STRENGTH);
        translateTable.put("unluck", Effects.UNLUCK);
        translateTable.put("water_breathing", Effects.WATER_BREATHING);
        translateTable.put("weakness", Effects.WEAKNESS);
        translateTable.put("wither", Effects.WITHER);
        return translateTable;
    }
}
