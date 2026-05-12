package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Generiert pro Eintrag in {@code assets/<modid>/fluiddefinitions/*.json}
 * die im 1.14.4-Resource-Pack vorausgesetzten Asset-Files fuer einen
 * Custom-Fluid:
 *
 * <ul>
 *   <li>{@code blockstates/<name>.json} mit einem leeren Default-Variant,
 *       der auf das generierte Block-Modell zeigt -- der eigentliche
 *       Render passiert ueber den Vanilla-FluidRenderer, das Modell
 *       liefert nur die Partikel-Textur.</li>
 *   <li>{@code models/block/<name>.json} ohne Geometrie, mit einer
 *       {@code particle}-Textur auf {@code blocks/<name>_still}.</li>
 *   <li>{@code models/item/<name>_bucket.json} fuer das Bucket-Item; nutzt
 *       die Vanilla-Bucket-Textur als Layer 0.</li>
 * </ul>
 *
 * Damit bleiben die Source-Resources unter {@code src/main/resources/}
 * unveraendert und der Mod-Author muss die Block-/Modellfiles fuer Fluids
 * nicht von Hand pflegen.
 */
class FluidAssetGenStep extends AssetMigrationStep {

    FluidAssetGenStep() {
        super('fluid-asset-gen')
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
            writeFluidAssets(ctx, modid, fluidName)
        }
    }

    private static void writeFluidAssets(final MigrationContext ctx, final String modid,
            final String fluidName) {
        ctx.write("assets/${modid}/blockstates/${fluidName}.json",
                JsonOutput.prettyPrint(JsonOutput.toJson([
                        variants: ['': [model: "${modid}:block/${fluidName}".toString()]]
                ])))

        ctx.write("assets/${modid}/models/block/${fluidName}.json",
                JsonOutput.prettyPrint(JsonOutput.toJson([
                        textures: [particle: "${modid}:blocks/${fluidName}_still".toString()]
                ])))

        // Bucket-Model nutzt Forges DynamicBucketModel (Loader "forge:bucket"),
        // der den Eimer-Look mit der zum Fluid gehoerenden Mask faerbt -- das
        // entspricht dem Wasser-/Lava-Eimer-Verhalten. Der Loader ist seit
        // 1.14.4-28.x out of the box bei Forge dabei.
        ctx.write("assets/${modid}/models/item/${fluidName}_bucket.json",
                JsonOutput.prettyPrint(JsonOutput.toJson([
                        parent: 'forge:item/bucket',
                        loader: 'forge:bucket',
                        fluid : "${modid}:${fluidName}".toString()
                ])))
    }
}
