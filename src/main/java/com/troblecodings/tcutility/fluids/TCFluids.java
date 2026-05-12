package com.troblecodings.tcutility.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.item.BucketItem;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/**
 * Bundle der vier Komponenten, aus denen ein Custom-Fluid in 1.14.4 besteht:
 * eine Quell-Variante ({@link BaseFlowingFluid.Source}), eine Flow-Variante
 * ({@link BaseFlowingFluid.Flowing}), der zugehoerige Block
 * ({@link LiquidBlock}) und das Bucket-Item ({@link BucketItem}).
 *
 * Wird von {@link com.troblecodings.tcutility.init.TCFluidsInit} pro JSON-
 * Eintrag aus {@code fluiddefinitions/} erzeugt.
 */
public class TCFluids {

    public final String name;
    public final BaseFlowingFluid.Source source;
    public final BaseFlowingFluid.Flowing flowing;
    public final LiquidBlock block;
    public final BucketItem bucket;

    public TCFluids(final String name, final BaseFlowingFluid.Source source,
            final BaseFlowingFluid.Flowing flowing, final LiquidBlock block,
            final BucketItem bucket) {
        this.name = name;
        this.source = source;
        this.flowing = flowing;
        this.block = block;
        this.bucket = bucket;
    }
}
