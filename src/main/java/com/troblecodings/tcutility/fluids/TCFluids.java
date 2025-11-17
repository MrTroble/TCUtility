package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.utils.FluidCreateInfo;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class TCFluids extends Fluid {

    public TCFluids(final String fluidName, final ResourceLocation still,
            final ResourceLocation flowing, final ResourceLocation overlay,
            final FluidCreateInfo fluidInfo) {
        super(fluidName, still, flowing, overlay);

        this.setUnlocalizedName(fluidName);
        this.density = fluidInfo.density;
        this.temperature = fluidInfo.temperature;
        this.viscosity = fluidInfo.viscosity;
        this.isGaseous = fluidInfo.density > 0 ? false : true;
        this.luminosity = fluidInfo.luminosity;
        this.color = fluidInfo.color;

    }

}
