package com.troblecodings.tcutility.fluids;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 1.14.4-Implementierung des Mod-Fluid-Blocks. Erbt das gesamte Verhalten
 * von {@link FlowingFluidBlock} (Quell-/Flowstates, Slope-Find, Tickrate,
 * Bucket-Pickup) und ergaenzt nur den Status-Effect, der den ehemaligen
 * 1.12.2-{@code onEntityCollidedWithBlock}-Override ersetzt.
 */
public class TCFluidBlock extends FlowingFluidBlock {

    private final Effect effect;
    private final int durationSeconds;
    private final int amplifier;

    public TCFluidBlock(final Supplier<? extends FlowingFluid> supplier,
            final Block.Properties properties, final Effect effect,
            final int durationSeconds, final int amplifier) {
        super(supplier, properties);
        this.effect = effect;
        this.durationSeconds = durationSeconds;
        this.amplifier = amplifier;
    }

    @Override
    public void onEntityCollision(final BlockState state, final World world, final BlockPos pos,
            final Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (effect != null && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(effect,
                    Math.max(1, durationSeconds) * 20, Math.max(0, amplifier - 1)));
        }
    }
}
