package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Bringt Slab-/Stair-/Trapdoor-Blockstates von 1.12-Properties auf das Vanilla-1.13+-Schema:
 *
 * <ul>
 *   <li>Slabs: 1.12 hatte einen "half=top|bottom|double"-State; ab 1.13 heisst die Property
 *       {@code type} und der Block hat zusaetzlich {@code waterlogged=true|false}.</li>
 *   <li>Stairs: 1.12 hatte {@code facing,half,shape}; ab 1.13 kommt {@code waterlogged} dazu --
 *       die Property-Bezeichner bleiben sonst gleich.</li>
 *   <li>Trapdoors: 1.12 hatte {@code facing,half,open,powered}; ab 1.14 kommt
 *       {@code waterlogged} dazu.</li>
 * </ul>
 *
 * Dieser Step laeuft nach {@link ForgeMarkerBlockstateStep} (der die Variants schon ins
 * Vanilla-Format gebracht, aber die Keys 1:1 belassen hat) und erwartet daher eine flache
 * {@code variants}-Struktur. Models werden dabei nicht angefasst -- jeder generierte Variant-
 * Eintrag zeigt fuer beide Waterlogged-Werte auf dasselbe Model, was visuell mit dem Verhalten
 * vom 1.12-Modell uebereinstimmt.
 */
class BlockstateWaterloggedStep extends AssetMigrationStep {

    BlockstateWaterloggedStep() {
        super('blockstate-waterlogged')
    }

    @Override
    boolean matches(final String relPath) {
        if (!(relPath ==~ ~/^assets\/[^\/]+\/blockstates\/.+\.json$/)) {
            return false
        }
        final String name = relPath.substring(relPath.lastIndexOf('/') + 1)
        return name.startsWith('slab_') || name.startsWith('stair_') || name.startsWith('trapdoor_')
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final def parsed = new JsonSlurper().parse(file)
        if (!(parsed instanceof Map)) {
            return
        }
        final Map json = (Map) parsed
        final Object variantsObj = json.variants
        if (!(variantsObj instanceof Map)) {
            return
        }
        final Map variants = (Map) variantsObj

        final String name = relPath.substring(relPath.lastIndexOf('/') + 1)
        final boolean isSlab = name.startsWith('slab_')

        final Map<String, Object> rewritten = [:]
        for (final Map.Entry vent : variants.entrySet()) {
            final String oldKey = vent.key as String
            final Object value = vent.value

            String newKeyBase
            if (isSlab) {
                // Slabs: das einzige State-Property im 1.12-Schema ist half -- direkt umbenennen.
                if (oldKey == '' || oldKey == null) {
                    // ForgeMarkerBlockstateStep kann fuer Inventory-only-Slabs einen leeren
                    // Default-Key schreiben; den lassen wir liegen, weil ohne type-Property
                    // kein State zu treffen waere.
                    rewritten[oldKey] = value
                    continue
                }
                newKeyBase = oldKey.replaceFirst(/^half=/, 'type=').replaceAll(/,half=/, ',type=')
            } else {
                // Stairs/Trapdoors: half bleibt, nur waterlogged anhaengen.
                newKeyBase = oldKey
            }

            rewritten["${newKeyBase},waterlogged=false".toString()] = value
            rewritten["${newKeyBase},waterlogged=true".toString()] = value
        }

        json.variants = rewritten
        ctx.write(relPath, JsonOutput.prettyPrint(JsonOutput.toJson(json)))
    }
}
