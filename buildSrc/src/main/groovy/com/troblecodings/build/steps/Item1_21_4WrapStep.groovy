package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput

/**
 * Seit 1.21.2 gibt es einen separaten Item-Model-Layer unter
 * {@code assets/<modid>/items/<id>.json}, der den eigentlichen Renderer-Datensatz pro Item
 * bereitstellt. Vorher reichte ein {@code models/item/<id>.json}, jetzt sucht der Item-Renderer
 * primaer den neuen Pfad und faellt sonst auf das Missing-Texture-Visual zurueck.
 *
 * <p>Dieser Step erzeugt fuer jedes vorhandene Item-Modell unter {@code models/item/<id>.json}
 * eine schlanke Wrapper-Datei {@code items/<id>.json}:
 * <pre>{ "model": { "type": "minecraft:model", "model": "&lt;modid&gt;:item/&lt;id&gt;" } }</pre>
 * Bucket-Items haben einen eigenen Codec-basierten Pfad ({@link DynamicBucketItemModelStep}) --
 * fuer die wird hier kein Wrapper geschrieben.
 */
class Item1_21_4WrapStep extends AssetMigrationStep {

    Item1_21_4WrapStep() {
        super('item-1-21-4-wrap')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/models\/item\/[^\/]+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String[] segs = relPath.split('/')
        final String modid = segs[1]
        final String basename = segs[-1].replaceAll(/\.json$/, '')

        // Bucket-Items kriegen ihren eigenen ItemModel-Codec (DynamicBucketItemModelStep);
        // damit verhindern wir hier den unsinnigen "minecraft:model"-Wrapper, der den alten
        // Forge-Loader-Eintrag wieder reinziehen wuerde.
        if (basename.endsWith('_bucket')) {
            return
        }

        final String wrapperPath = "assets/${modid}/items/${basename}.json"
        final Map wrapper = [
                model: [
                        type : 'minecraft:model',
                        model: "${modid}:item/${basename}".toString()
                ]
        ]
        ctx.write(wrapperPath, JsonOutput.prettyPrint(JsonOutput.toJson(wrapper)))
    }
}
