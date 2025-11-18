package com.troblecodings.tcutility.utils;

public class FluidProperties {

    private int luminosity;
    private int density;
    private int temperature;
    private int viscosity;

    public FluidCreateInfo getFluidInfo() {
        return new FluidCreateInfo(luminosity, density, temperature, viscosity);
    }
}
