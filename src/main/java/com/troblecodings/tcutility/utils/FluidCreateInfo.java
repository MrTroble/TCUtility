package com.troblecodings.tcutility.utils;

public class FluidCreateInfo {

    public final int luminosity;
    public final int density;
    public final int temperature;
    public final int viscosity;
    public final int flowLength;
    public final boolean canCreateSource;

    public FluidCreateInfo(final int luminosity, final int density, final int temperature,
            final int viscosity, final int flowLength, final boolean canCreateSource) {
        this.luminosity = luminosity;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
        this.flowLength = flowLength;
        this.canCreateSource = canCreateSource;
    }

}
