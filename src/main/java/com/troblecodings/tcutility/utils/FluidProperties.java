package com.troblecodings.tcutility.utils;

import java.util.HashMap;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

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

    private static final HashMap<String, Potion> POTION_TABLE = translateTablePotion();

    public FluidCreateInfo getFluidInfo() {
        final Potion effectPotion = POTION_TABLE.get(effect.toLowerCase());
        return new FluidCreateInfo(luminosity, density, temperature, viscosity, flowLength,
                canCreateSource, effectPotion, effectDuration, effectAmplifier);
    }

    private static HashMap<String, Potion> translateTablePotion() {
        final HashMap<String, Potion> translateTable = new HashMap<>();
        translateTable.put("", null);
        translateTable.put("absorption", MobEffects.ABSORPTION);
        translateTable.put("blindness", MobEffects.BLINDNESS);
        translateTable.put("fire_resistance", MobEffects.FIRE_RESISTANCE);
        translateTable.put("glowing", MobEffects.GLOWING);
        translateTable.put("haste", MobEffects.HASTE);
        translateTable.put("health_boost", MobEffects.HEALTH_BOOST);
        translateTable.put("hunger", MobEffects.HUNGER);
        translateTable.put("instant_damage", MobEffects.INSTANT_DAMAGE);
        translateTable.put("instant_health", MobEffects.INSTANT_HEALTH);
        translateTable.put("invisibility", MobEffects.INVISIBILITY);
        translateTable.put("jump_boost", MobEffects.JUMP_BOOST);
        translateTable.put("levitation", MobEffects.LEVITATION);
        translateTable.put("luck", MobEffects.LUCK);
        translateTable.put("mining_fatigue", MobEffects.MINING_FATIGUE);
        translateTable.put("nausea", MobEffects.NAUSEA);
        translateTable.put("night_vision", MobEffects.NIGHT_VISION);
        translateTable.put("poison", MobEffects.POISON);
        translateTable.put("regeneration", MobEffects.REGENERATION);
        translateTable.put("resistance", MobEffects.RESISTANCE);
        translateTable.put("saturation", MobEffects.SATURATION);
        translateTable.put("slowness", MobEffects.SLOWNESS);
        translateTable.put("speed", MobEffects.SPEED);
        translateTable.put("strength", MobEffects.STRENGTH);
        translateTable.put("unluck", MobEffects.UNLUCK);
        translateTable.put("water_breathing", MobEffects.WATER_BREATHING);
        translateTable.put("weakness", MobEffects.WEAKNESS);
        translateTable.put("wither", MobEffects.WITHER);
        return translateTable;
    }
}
