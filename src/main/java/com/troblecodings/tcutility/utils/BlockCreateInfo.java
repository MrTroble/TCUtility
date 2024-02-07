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

    public BlockCreateInfo(final Material material, final float hardness, final SoundType soundtype,
            final int opacity, final int lightValue, List<Integer> box) {
        this.material = material;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
        this.lightValue = lightValue;
        this.box = box;
    }
}