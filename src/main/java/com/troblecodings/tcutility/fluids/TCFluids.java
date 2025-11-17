package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.utils.FluidCreateInfo;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class TCFluids extends Fluid {

	public TCFluids(String fluidName, ResourceLocation still, ResourceLocation flowing, FluidCreateInfo fluidInfo) {
		super(fluidName, still, flowing);

		this.setUnlocalizedName(fluidName);
		this.density = fluidInfo.density;
		this.temperature = fluidInfo.temperature;
		this.viscosity = fluidInfo.viscosity;
		this.isGaseous = fluidInfo.isGaseous;
		this.luminosity = fluidInfo.luminosity;
		this.color = fluidInfo.color;
		
	}

}
