package com.troblecodings.tcutility.utils;

import java.util.HashMap;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

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

    private static final HashMap<String, Holder<MobEffect>> EFFECT_TABLE = translateTableEffect();

    public FluidCreateInfo getFluidInfo() {
        final Holder<MobEffect> effectInstance = EFFECT_TABLE.get(effect.toLowerCase());
        return new FluidCreateInfo(luminosity, density, temperature, viscosity, flowLength,
                canCreateSource, effectInstance, effectDuration, effectAmplifier);
    }

    private static HashMap<String, Holder<MobEffect>> translateTableEffect() {
        // 1.21: MobEffects-Konstanten sind {@link Holder}<MobEffect>-Wrapper; JSON-facing keys
        // behalten ihre 1.12-Namen, damit Content-Packs unveraendert weiterfunktionieren.
        final HashMap<String, Holder<MobEffect>> translateTable = new HashMap<>();
        translateTable.put("", null);
        translateTable.put("absorption", MobEffects.ABSORPTION);
        translateTable.put("blindness", MobEffects.BLINDNESS);
        translateTable.put("fire_resistance", MobEffects.FIRE_RESISTANCE);
        translateTable.put("glowing", MobEffects.GLOWING);
        translateTable.put("haste", MobEffects.DIG_SPEED);
        translateTable.put("health_boost", MobEffects.HEALTH_BOOST);
        translateTable.put("hunger", MobEffects.HUNGER);
        translateTable.put("instant_damage", MobEffects.HARM);
        translateTable.put("instant_health", MobEffects.HEAL);
        translateTable.put("invisibility", MobEffects.INVISIBILITY);
        translateTable.put("jump_boost", MobEffects.JUMP);
        translateTable.put("levitation", MobEffects.LEVITATION);
        translateTable.put("luck", MobEffects.LUCK);
        translateTable.put("mining_fatigue", MobEffects.DIG_SLOWDOWN);
        translateTable.put("nausea", MobEffects.CONFUSION);
        translateTable.put("night_vision", MobEffects.NIGHT_VISION);
        translateTable.put("poison", MobEffects.POISON);
        translateTable.put("regeneration", MobEffects.REGENERATION);
        translateTable.put("resistance", MobEffects.DAMAGE_RESISTANCE);
        translateTable.put("saturation", MobEffects.SATURATION);
        translateTable.put("slowness", MobEffects.MOVEMENT_SLOWDOWN);
        translateTable.put("speed", MobEffects.MOVEMENT_SPEED);
        translateTable.put("strength", MobEffects.DAMAGE_BOOST);
        translateTable.put("unluck", MobEffects.UNLUCK);
        translateTable.put("water_breathing", MobEffects.WATER_BREATHING);
        translateTable.put("weakness", MobEffects.WEAKNESS);
        translateTable.put("wither", MobEffects.WITHER);
        return translateTable;
    }
}
