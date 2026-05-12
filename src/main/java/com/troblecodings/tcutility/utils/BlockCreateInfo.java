package com.troblecodings.tcutility.utils;

import java.util.List;

import net.minecraft.block.AbstractBlock;
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
        return toProperties(false);
    }

    /**
     * Liefert Properties fuer Block-Klassen, deren Modell nie ein voller
     * Wuerfel ist (Stairs, Slabs, Fences, Walls, Ladders, Hangings,
     * Doors, Trapdoors, Windows, Pipes via Cube-Rot etc.). Ruft immer
     * {@code notSolid()}, damit der 1.16.5-Renderer adjacent-Faces nicht
     * cullt -- in 1.12.2 wurde das ueber das Material gesteuert, aber zB.
     * {@code Material.IRON.isOpaque() == true} reicht in 1.16.5 nicht mehr.
     */
    public Block.Properties toNonSolidProperties() {
        return toProperties(true);
    }

    private Block.Properties toProperties(final boolean forceNotSolid) {
        // 1.16.5: AbstractBlock.Properties hat notSolid() direkt -- der
        // Material.Builder-Workaround aus der 1.14.4-Variante ist hier nicht
        // mehr noetig. setLightLevel erwartet seit 1.15 eine ToIntFunction
        // pro BlockState, wir liefern einen konstanten Wert aus dem JSON.
        AbstractBlock.Properties props = AbstractBlock.Properties.create(material)
                .hardnessAndResistance(hardness)
                .sound(soundtype)
                .setLightLevel(state -> lightValue);
        // 1.12.2 leitete "ist dieser Block opak" implizit aus dem Material ab
        // -- Material.GLASS / ICE / etc. waren automatisch nicht-opak, was
        // dafuer sorgte, dass adjacent floors *nicht* face-gecullt wurden.
        // 1.16.5 ist Material.GLASS.isOpaque() jedoch true; ohne expliziten
        // notSolid()-Aufruf cullt der Renderer die Up-Face des Blocks unter
        // dem Lantern -- der Boden "verschwindet".
        if (forceNotSolid || !fullblock || isTransparentMaterial(material)) {
            props = props.notSolid();
        }
        return props;
    }

    private static boolean isTransparentMaterial(final Material mat) {
        return mat == Material.GLASS || mat == Material.ICE || mat == Material.PACKED_ICE
                || mat == Material.LEAVES || mat == Material.PLANTS;
    }
}
