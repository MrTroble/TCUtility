package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class TCFluidBlock extends BlockFluidClassic {

	public TCFluidBlock(Fluid fluid, Material material) {
		super(fluid, material);
		
		setCreativeTab(TCTabs.FLUIDS);
	}

	
}
