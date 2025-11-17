package com.troblecodings.tcutility.utils;

public class FluidProperties {

	private int luminosity;
	private int density;
	private int temperature;
	private int viscosity;
	private int color;
	private boolean isGaseous;
	
	
	public FluidCreateInfo getFluidInfo() {
		return new FluidCreateInfo(luminosity, density, temperature, viscosity, color, isGaseous);
	}
}

