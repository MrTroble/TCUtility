package com.troblecodings.build

import com.troblecodings.build.steps.BlockstateWaterloggedStep
import com.troblecodings.build.steps.BucketLoaderRenameStep
import com.troblecodings.build.steps.DynamicBucketItemModelStep
import com.troblecodings.build.steps.FenceWallTagGenStep
import com.troblecodings.build.steps.FixHashedTextureKeysStep
import com.troblecodings.build.steps.FluidAssetGenStep
import com.troblecodings.build.steps.ForgeMarkerBlockstateStep
import com.troblecodings.build.steps.Item1_21_4WrapStep
import com.troblecodings.build.steps.NeoForgeNamespaceStep
import com.troblecodings.build.steps.VanillaPostFlatteningRenameStep
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
                new BlockstateWaterloggedStep(),
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

    /**
     * Migrationen 1.16 -> 1.19. pack_format wechselt von 6 auf 9. Vanilla
     * benennt zwischen 1.16 und 1.19 einige Texturen um (1.17:
     * {@code grass_path} wird zu {@code dirt_path}); das laeuft post-
     * flattening, weil die Refs an dieser Stelle bereits {@code minecraft:block/X}
     * (Singular) sind.
     */
    static List<AssetMigrationStep> mc1_16_to_mc1_19() {
        return [
                new VanillaPostFlatteningRenameStep(),
        ]
    }

    /**
     * Migrationen 1.19 -> 1.20. Forge hat den DynamicBucketModel-Loader von
     * {@code forge:bucket} auf {@code forge:fluid_container} umbenannt; das
     * generierte Bucket-Model muss entsprechend nachgezogen werden.
     */
    static List<AssetMigrationStep> mc1_19_to_mc1_20() {
        return [
                new BucketLoaderRenameStep(),
        ]
    }

    /**
     * Migrationen 1.20 -> 1.21 (NeoForge). NeoForge hat den Forge-Namespace aus den
     * Builtin-Loadern und -Models entfernt; {@code forge:fluid_container} heisst jetzt
     * {@code neoforge:fluid_container}, das Bucket-Parent-Model {@code forge:item/bucket}
     * heisst {@code neoforge:item/bucket}. Im Forge-1.21-Build (sollte er kommen) wird der
     * Step nicht angehaengt -- nur die NeoForge-Pipeline laesst ihn laufen.
     */
    static List<AssetMigrationStep> mc1_20_to_mc1_21_neoforge() {
        return [
                new NeoForgeNamespaceStep(),
        ]
    }

    /**
     * Migrationen 1.21.1 -> 1.21.4. NeoForge hat ab 1.21.2 das Item-Model-Layout in
     * {@code assets/<modid>/items/<id>.json} verlagert (Codec-basiertes ItemModel-System).
     * Das alte {@code models/item/<id>.json} reicht alleine nicht mehr -- der Item-Renderer
     * sucht primaer den neuen Pfad. Der Bucket-Block-Loader {@code neoforge:fluid_container}
     * ist parallel auf einen Codec mit gleichem Namen gewandert; der alte Loader-Eintrag
     * existiert in 1.21.4 nicht mehr.
     */
    static List<AssetMigrationStep> mc1_21_1_to_mc1_21_4_neoforge() {
        return [
                new DynamicBucketItemModelStep(),
                new Item1_21_4WrapStep(),
        ]
    }
}
