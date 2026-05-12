package com.troblecodings.tcutility.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

/**
 * Mod-Fluid-Block. Erbt das gesamte Verhalten von {@link LiquidBlock} (Quell-/Flowstates,
 * Slope-Find, Tickrate, Bucket-Pickup) und ergaenzt nur den Status-MobEffect, der den
 * ehemaligen 1.12.2-{@code onEntityCollidedWithBlock}-Override ersetzt.
 *
 * <p>1.21: {@link LiquidBlock}-Ctor nimmt jetzt direkt die {@link FlowingFluid}-Instanz statt
 * eines Suppliers; {@link MobEffectInstance}-Ctor erwartet einen {@link Holder}-Wrapper um
 * den Effekt.
 */
public class TCFluidBlock extends LiquidBlock {

    private final Holder<MobEffect> effect;
    private final int durationSeconds;
    private final int amplifier;

    public TCFluidBlock(final FlowingFluid fluid, final Block.Properties properties,
            final Holder<MobEffect> effect, final int durationSeconds, final int amplifier) {
        super(fluid, properties);
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
