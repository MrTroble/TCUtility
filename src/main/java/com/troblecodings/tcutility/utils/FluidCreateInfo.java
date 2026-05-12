package com.troblecodings.tcutility.utils;

import net.minecraft.potion.Effect;

public class FluidCreateInfo {

    public final int luminosity;
    public final int density;
    public final int temperature;
    public final int viscosity;
    public final int flowLength;
    public final boolean canCreateSource;
    public final Effect effect;
    public final int effectDuration;
    public final int effectAmplifier;

    public FluidCreateInfo(final int luminosity, final int density, final int temperature,
            final int viscosity, final int flowLength, final boolean canCreateSource,
            final Effect effect, final int effectDuration, final int effectAmplifier) {
        this.luminosity = luminosity;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
        this.flowLength = flowLength;
        this.canCreateSource = canCreateSource;
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
    }
}
