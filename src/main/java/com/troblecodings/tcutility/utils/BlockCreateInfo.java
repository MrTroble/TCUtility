package com.troblecodings.tcutility.utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCreateInfo {

    public final Material material;
    public final float hardness;
    public final SoundType soundtype;
    public final int opacity;
    public final int lightValue;
    public final List<Integer> box;
    public final boolean fullblock;

    public BlockCreateInfo(final Material material, final float hardness, final SoundType soundtype,
            final int opacity, final int lightValue, final List<Integer> box,
            final boolean fullblock) {
        this.material = material;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
        this.lightValue = lightValue;
        this.box = box;
        this.fullblock = fullblock;
    }

    public Block.Properties toProperties() {
        // 1.14.4-28.2.28: Block.Properties hat noch kein notSolid() /
        // noOcclusion() (das kam mit 1.15). Stattdessen wird die Opazitaet
        // ueber das Material gesteuert -- Material.Builder.notSolid() setzt
        // die Material-Flags isSolid+isOpaque auf false, wovon Vanilla das
        // Light-Propagation- und Render-Verhalten ableitet.
        //
        // Wenn das JSON fullblock:false setzt, bauen wir hier ein Material-
        // Derivat mit notSolid() ohne die anderen Eigenschaften des
        // Originalmaterials zu verlieren (liquid, doesNotBlockMovement,
        // replaceable). Wenn das Originalmaterial sowieso bereits notSolid
        // ist (z.B. Material.GLASS, Material.LEAVES), liefert notSolid()
        // dasselbe Ergebnis -- der Aufruf ist also idempotent.
        final Material mat = fullblock ? material : asNotSolid(material);
        return Block.Properties.create(mat)
                .hardnessAndResistance(hardness)
                .sound(soundtype)
                .lightValue(lightValue);
    }

    private static Material asNotSolid(final Material original) {
        if (!original.isSolid() && !original.isOpaque()) {
            return original;
        }
        final Material.Builder builder = new Material.Builder(original.getColor())
                .notSolid();
        if (original.isLiquid()) {
            builder.liquid();
        }
        if (!original.blocksMovement()) {
            builder.doesNotBlockMovement();
        }
        if (original.isReplaceable()) {
            builder.replaceable();
        }
        return builder.build();
    }
}
