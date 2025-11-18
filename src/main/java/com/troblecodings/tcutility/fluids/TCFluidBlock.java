package com.troblecodings.tcutility.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class TCFluidBlock extends BlockFluidClassic {

    private final TCFluids fluid;

    public TCFluidBlock(final Fluid fluid) {
        super(fluid, Material.WATER);
        this.fluid = (TCFluids) fluid;
    }

    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public int getQuantaValue(final IBlockAccess world, final BlockPos pos) {
        System.out.println(super.getQuantaValue(world, pos));
        if (isSourceBlock(world, pos))
            return Math.max(2, Math.min(8, fluid.flowLength));
        return super.getQuantaValue(world, pos);
    }
}
