package com.troblecodings.tcutility.utils;

import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockCreateInfo {

    public final MaterialKind kind;
    public final float hardness;
    public final SoundType soundtype;
    public final int opacity;
    public final int lightValue;
    public final List<Integer> box;
    public final boolean fullblock;

    /**
     * 1.21.2+: Block.Properties verlangt {@code setId(ResourceKey<Block>)} vor jedem Block-
     * Konstruktor (Block.<init> ruft {@code effectiveDrops()} und NPEt sonst). Wir merken uns
     * den Key transient hier und {@link #toProperties} setzt ihn auf den Properties-Build. Per
     * {@link com.troblecodings.tcutility.init.TCBlocks#onRegister} wird das Feld unmittelbar
     * vor jedem {@code constructBlock}-Aufruf neu gesetzt; mehrere BlockSpecs koennen sich die
     * Info teilen, der Key wird daher pro Iteration ueberschrieben.
     */
    public ResourceKey<Block> blockKey;

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
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(kind.mapColor())
                .strength(hardness)
                .sound(soundtype)
                .lightLevel(state -> lightValue);
        if (forceNotSolid || !fullblock || kind.isTransparent()) {
            props = props.noOcclusion();
        }
        if (blockKey != null) {
            props = props.setId(blockKey);
        }
        return props;
    }
}
