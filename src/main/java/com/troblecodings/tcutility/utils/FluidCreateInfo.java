package com.troblecodings.tcutility.utils;

public class FluidCreateInfo {

    public final int luminosity;
    public final int density;
    public final int temperature;
    public final int viscosity;

    public FluidCreateInfo(final int luminosity, final int density, final int temperature,
            final int viscosity) {
        this.luminosity = luminosity;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
    }

}
