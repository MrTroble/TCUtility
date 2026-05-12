package com.troblecodings.tcutility.utils;

import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BlockCreateInfo {

    public final MaterialKind kind;
    public final float hardness;
    public final SoundType soundtype;
    public final int opacity;
    public final int lightValue;
    public final List<Integer> box;
    public final boolean fullblock;

    public BlockCreateInfo(final MaterialKind kind, final float hardness, final SoundType soundtype,
            final int opacity, final int lightValue, final List<Integer> box,
            final boolean fullblock) {
        this.kind = kind;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
        this.lightValue = lightValue;
        this.box = box;
        this.fullblock = fullblock;
    }

    public Block.Properties toProperties() {
        return toProperties(false);
    }

    /**
     * Properties fuer Block-Klassen, deren Modell nie ein voller Wuerfel ist (Stairs, Slabs,
     * Fences, Walls, Ladders, Hangings, Doors, Trapdoors, Windows, Pipes via Cube-Rot etc.).
     * Ruft immer {@code noOcclusion()}, damit der Renderer adjacent-Faces nicht cullt.
     */
    public Block.Properties toNonSolidProperties() {
        return toProperties(true);
    }

    private Block.Properties toProperties(final boolean forceNotSolid) {
        // 1.20: Properties.of() nimmt keinen Material-Parameter mehr; MapColor wird separat
        // gesetzt. Sonstige Eigenschaften wie Sound, Strength, Light bleiben unveraendert.
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(kind.mapColor())
                .strength(hardness)
                .sound(soundtype)
                .lightLevel(state -> lightValue);
        if (forceNotSolid || !fullblock || kind.isTransparent()) {
            props = props.noOcclusion();
        }
        return props;
    }
}
