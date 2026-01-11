package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.utils.FluidCreateInfo;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class TCFluids extends Fluid {

    public final int flowLength;
    public final boolean canCreateSource;
    public final Potion effectPotion;
    public final int effectDuration;
    public final int effectAmplifier;

    public TCFluids(final String fluidName, final ResourceLocation still,
            final ResourceLocation flowing,
            final FluidCreateInfo fluidInfo) {
        super(fluidName, still, flowing);

        this.setUnlocalizedName(fluidName);
        this.density = fluidInfo.density;
        this.temperature = fluidInfo.temperature;
        this.viscosity = fluidInfo.viscosity;
        this.isGaseous = fluidInfo.density >= 0 ? false : true;
        this.luminosity = fluidInfo.luminosity;
        this.flowLength = fluidInfo.flowLength;
        this.canCreateSource = fluidInfo.canCreateSource;
        this.effectPotion = fluidInfo.effectPotion;
        this.effectDuration = fluidInfo.effectDuration;
        this.effectAmplifier = fluidInfo.effectAmplifier;
    }
}
