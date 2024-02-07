package com.troblecodings.tcutility.utils;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCreateInfo {

    public Material material;
    public float hardness;
    public SoundType soundtype;
    public int opacity;
    public int lightValue;
    public List<Integer> box;
    public boolean fullblock;
    public float slipperness;

    public BlockCreateInfo(final Material material, final float hardness, final SoundType soundtype,
            final int opacity, final int lightValue, final List<Integer> box, final boolean fullblock, final float slipperness) {
        this.material = material;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
        this.lightValue = lightValue;
        this.box = box;
        this.fullblock = fullblock;
        this.slipperness = slipperness;
    }
}