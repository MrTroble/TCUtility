package com.troblecodings.tcutility.fluids;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * Bundle der vier Komponenten, aus denen ein Custom-Fluid in 1.14.4 besteht:
 * eine Quell-Variante (vanilla {@link FlowingFluid}-Subklasse, fuer normale
 * Fluide {@link ForgeFlowingFluid.Source}, fuer Gas-Fluide
 * {@link TCUpwardFlowingFluid.Source}), eine Flow-Variante, der zugehoerige
 * Block ({@link FlowingFluidBlock}) und das Bucket-Item ({@link BucketItem}).
 *
 * Wird von {@link com.troblecodings.tcutility.init.TCFluidsInit} pro JSON-
 * Eintrag aus {@code fluiddefinitions/} erzeugt.
 */
public class TCFluids {

    public final String name;
    public final FlowingFluid source;
    public final FlowingFluid flowing;
    public final FlowingFluidBlock block;
    public final BucketItem bucket;

    public TCFluids(final String name, final FlowingFluid source,
            final FlowingFluid flowing, final FlowingFluidBlock block,
            final BucketItem bucket) {
        this.name = name;
        this.source = source;
        this.flowing = flowing;
        this.block = block;
        this.bucket = bucket;
    }
}
