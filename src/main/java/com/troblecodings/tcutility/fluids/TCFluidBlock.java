package com.troblecodings.tcutility.fluids;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * 1.14.4-Implementierung des Mod-Fluid-Blocks. Erbt das gesamte Verhalten
 * von {@link LiquidBlock} (Quell-/Flowstates, Slope-Find, Tickrate,
 * Bucket-Pickup) und ergaenzt nur den Status-MobEffect, der den ehemaligen
 * 1.12.2-{@code onEntityCollidedWithBlock}-Override ersetzt.
 */
public class TCFluidBlock extends LiquidBlock {

    private final MobEffect effect;
    private final int durationSeconds;
    private final int amplifier;

    public TCFluidBlock(final Supplier<? extends FlowingFluid> supplier,
            final Block.Properties properties, final MobEffect effect,
            final int durationSeconds, final int amplifier) {
        super(supplier, properties);
        this.effect = effect;
        this.durationSeconds = durationSeconds;
        this.amplifier = amplifier;
    }

    @Override
    public void entityInside(final BlockState state, final Level world, final BlockPos pos,
            final Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (effect != null && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(effect,
                    Math.max(1, durationSeconds) * 20, Math.max(0, amplifier - 1)));
        }
    }
}
