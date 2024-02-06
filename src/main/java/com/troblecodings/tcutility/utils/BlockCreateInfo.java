package com.troblecodings.tcutility.utils;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCreateInfo {

    public Material material;
    public float hardness;
    public SoundType soundtype;
    public int opacity;
    public int lightValue;

    public BlockCreateInfo(final Material material, final float hardness, final SoundType soundtype,
            final int opacity, final int lightValue) {
        this.material = material;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
        this.lightValue = lightValue;
    }
}