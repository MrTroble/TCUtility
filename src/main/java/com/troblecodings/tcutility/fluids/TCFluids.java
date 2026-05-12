package com.troblecodings.tcutility.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.item.BucketItem;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * Bundle der vier Komponenten, aus denen ein Custom-Fluid in 1.14.4 besteht:
 * eine Quell-Variante ({@link ForgeFlowingFluid.Source}), eine Flow-Variante
 * ({@link ForgeFlowingFluid.Flowing}), der zugehoerige Block
 * ({@link LiquidBlock}) und das Bucket-Item ({@link BucketItem}).
 *
 * Wird von {@link com.troblecodings.tcutility.init.TCFluidsInit} pro JSON-
 * Eintrag aus {@code fluiddefinitions/} erzeugt.
 */
public class TCFluids {

    public final String name;
    public final ForgeFlowingFluid.Source source;
    public final ForgeFlowingFluid.Flowing flowing;
    public final LiquidBlock block;
    public final BucketItem bucket;

    public TCFluids(final String name, final ForgeFlowingFluid.Source source,
            final ForgeFlowingFluid.Flowing flowing, final LiquidBlock block,
            final BucketItem bucket) {
        this.name = name;
        this.source = source;
        this.flowing = flowing;
        this.block = block;
        this.bucket = bucket;
    }
}
