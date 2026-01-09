package com.troblecodings.tcutility.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class TCFluidBlock extends BlockFluidClassic {

    private final TCFluids fluid;

    public TCFluidBlock(final TCFluids fluid) {
        super(fluid, Material.WATER);
        this.fluid = fluid;
        this.canCreateSources = this.fluid.canCreateSource;
    }

    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public int getQuantaValue(final IBlockAccess world, final BlockPos pos) {
        if (isSourceBlock(world, pos))
            return Math.max(2, Math.min(8, fluid.flowLength));
        return super.getQuantaValue(world, pos);
    }

    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos,
            final IBlockState state, final Entity entity) {
        if (entity instanceof EntityLivingBase && this.fluid.effectPotion != null) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(this.fluid.effectPotion,
                    this.fluid.effectDuration * 20, this.fluid.effectAmplifier - 1));
        }
    }
}
