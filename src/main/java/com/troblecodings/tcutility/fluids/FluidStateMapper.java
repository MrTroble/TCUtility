package com.troblecodings.tcutility.fluids;

import com.troblecodings.tcutility.TCUtilityMain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;

public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

    public final ModelResourceLocation location;

    public FluidStateMapper(final String name) {
        location = new ModelResourceLocation(TCUtilityMain.MODID + ":" + name, "fluid");
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return location;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack stack) {
        return location;
    }
}
