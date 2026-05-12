package com.troblecodings.build

import com.troblecodings.build.steps.FenceWallTagGenStep
import com.troblecodings.build.steps.FixHashedTextureKeysStep
import com.troblecodings.build.steps.FluidAssetGenStep
import com.troblecodings.build.steps.ForgeMarkerBlockstateStep
import com.troblecodings.build.steps.VanillaTextureRefStep

/**
 * Registry der bekannten Migrations-Step-Listen pro Minecraft-Versions-
 * Uebergang. Neue Versionen kommen hier als zusaetzliche statische Methode
 * dazu (z.B. {@code mc1_14_to_mc1_16()}); jede liefert eine in sich
 * abgeschlossene Liste an Steps zurueck, die das build-Skript dann an die
 * Pipeline anhaengt.
 *
 * Wenn ein Mod ueber mehrere Versionen springt (z.B. 1.12 -> 1.16), kann der
 * Build die Listen einfach hintereinander in dieselbe Pipeline reinwerfen;
 * weil jede Liste nur ihre Format-spezifischen Konvertierungen kennt, bleibt
 * die Komposition trivial.
 */
class AssetMigrations {

    private AssetMigrations() {}

    /** Migrationen fuer Resources, die im 1.12.2-Format vorliegen, hin zu 1.14.4. */
    static List<AssetMigrationStep> mc1_12_to_mc1_14() {
        return [
                new FixHashedTextureKeysStep(),
                new ForgeMarkerBlockstateStep(),
                new VanillaTextureRefStep(),
                new FluidAssetGenStep(),
                new FenceWallTagGenStep(),
        ]
    }

    /**
     * Migrationen 1.14 -> 1.16. Aktuell leer, weil das Asset-Format zwischen
     * 1.14 und 1.16 fuer Block-/Item-Modelle und Blockstates praktisch
     * unveraendert ist; pack_format wird direkt in der Original-{@code
     * pack.mcmeta} gepflegt. Platzhalter, damit kuenftige Renames (z.B. neue
     * Vanilla-Texture-Pfade) hier sauber dazukommen koennen.
     */
    static List<AssetMigrationStep> mc1_14_to_mc1_16() {
        return []
    }
}
