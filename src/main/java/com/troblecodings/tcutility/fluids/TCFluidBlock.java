package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class TCFluidBlock extends BlockFluidClassic {

    public TCFluidBlock(final Fluid fluid, final Material material) {
        super(fluid, material);

        setCreativeTab(TCTabs.FLUIDS);
    }

    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
