package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * NeoForge 1.21.4 verlagert den dynamischen Bucket-Renderer aus dem alten Block-Model-Loader-
 * Pfad {@code models/item/<id>.json} (der den Loader {@code neoforge:fluid_container} hatte) in
 * das neue Item-Model-Layout unter {@code items/<id>.json} mit Codec-Typ
 * {@code neoforge:fluid_container}. Der alte Loader ist in 1.21.4 nicht mehr registriert; das
 * Bucket-Model muss daher komplett neu geschrieben werden.
 *
 * <p>Wir scannen die fluiddefinitions auf alle Fluid-Namen und schreiben pro Fluid die neue
 * {@code items/<fluid>_bucket.json}-Datei mit Bezug auf den registrierten Mod-Fluid und die
 * Standard-NeoForge-Mask-Texturen. Den alten {@code models/item/<fluid>_bucket.json}-Eintrag
 * loeschen wir, damit er nicht zusaetzlich vom Resource-Manager geladen wird (er triggerte
 * sonst den "Unknown loader"-Crash beim Resource-Reload).
 */
class DynamicBucketItemModelStep extends AssetMigrationStep {

    DynamicBucketItemModelStep() {
        super('dynamic-bucket-item-model')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/fluiddefinitions\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String[] segs = relPath.split('/')
        final String modid = segs[1]

        final def parsed = new JsonSlurper().parse(file)
        if (!(parsed instanceof Map)) {
            return
        }
        for (final Object key : ((Map) parsed).keySet()) {
            final String fluidName = key as String

            // Neuer ItemModel-Eintrag mit Codec-Typ neoforge:fluid_container.
            final Map itemModel = [
                    model: [
                            type    : 'neoforge:fluid_container',
                            fluid   : "${modid}:${fluidName}".toString(),
                            textures: [
                                    base : 'neoforge:item/mask/bucket_fluid',
                                    fluid: 'neoforge:item/mask/bucket_fluid_drip',
                                    cover: 'neoforge:item/mask/bucket_fluid_cover_drip'
                            ]
                    ]
            ]
            ctx.write("assets/${modid}/items/${fluidName}_bucket.json",
                    JsonOutput.prettyPrint(JsonOutput.toJson(itemModel)))

            // Altes Bucket-Block-Model loeschen (es haette in 1.21.4 den alten Loader-
            // Eintrag aufgewiesen und einen Resource-Reload-Crash ausgeloest).
            ctx.remove("assets/${modid}/models/item/${fluidName}_bucket.json")
        }
    }
}
