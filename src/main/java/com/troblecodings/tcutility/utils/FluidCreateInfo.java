package com.troblecodings.tcutility.utils;

import net.minecraft.potion.Potion;

public class FluidCreateInfo {

    public final int luminosity;
    public final int density;
    public final int temperature;
    public final int viscosity;
    public final int flowLength;
    public final boolean canCreateSource;
    public final Potion effectPotion;
    public final int effectDuration;
    public final int effectAmplifier;

    public FluidCreateInfo(final int luminosity, final int density, final int temperature,
            final int viscosity, final int flowLength, final boolean canCreateSource,
            final Potion effePotion, final int effectDuration, final int effectAmplifier) {
        this.luminosity = luminosity;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
        this.flowLength = flowLength;
        this.canCreateSource = canCreateSource;
        this.effectPotion = effePotion;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
    }

}
