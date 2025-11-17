package com.troblecodings.tcutility.utils;

public class FluidCreateInfo {

	public final int luminosity;
	public final int density;
	public final int temperature;
	public final int viscosity;
	public final int color;
	public final boolean isGaseous;
	
	public FluidCreateInfo(final int luminosity, final int density, final int temperature, final int viscosity, final int color, final boolean isGaseous) {
		this.luminosity = luminosity;
		this.density = density;
		this.temperature = temperature;
		this.viscosity = viscosity;
		this.color = color;
		this.isGaseous = isGaseous;
	}
	
}
