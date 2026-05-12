package com.troblecodings.tcutility.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class FluidCreateInfo {

    public final int luminosity;
    public final int density;
    public final int temperature;
    public final int viscosity;
    public final int flowLength;
    public final boolean canCreateSource;
    /** 1.21: MobEffect-Konstanten liefern {@link Holder}-Wrapper; MobEffectInstance-Ctor
     *  nimmt diesen Holder direkt entgegen. */
    public final Holder<MobEffect> effect;
    public final int effectDuration;
    public final int effectAmplifier;

    public FluidCreateInfo(final int luminosity, final int density, final int temperature,
            final int viscosity, final int flowLength, final boolean canCreateSource,
            final Holder<MobEffect> effect, final int effectDuration, final int effectAmplifier) {
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
