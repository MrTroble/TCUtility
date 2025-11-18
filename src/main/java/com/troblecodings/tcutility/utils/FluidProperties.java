package com.troblecodings.tcutility.utils;

public class FluidProperties {

    private int luminosity = 0;
    private int density = 1000;
    private int temperature = 295;
    private int viscosity = 1000;
    private int flowLength = 8;

    public FluidCreateInfo getFluidInfo() {
        return new FluidCreateInfo(luminosity, density, temperature, viscosity, flowLength);
    }
}
