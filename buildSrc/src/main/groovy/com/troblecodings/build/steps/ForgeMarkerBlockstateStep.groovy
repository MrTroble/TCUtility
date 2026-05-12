package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Konvertiert Forge-1.12-Blockstates ({@code "forge_marker": 1}) in das
 * Vanilla-1.14-Blockstate-Format. Die alten {@code defaults}/{@code variants}-
 * Strukturen erlauben {@code model}+{@code textures}-Substitution direkt im
 * Blockstate; in 1.14.4 muss diese Substitution in einem separaten Child-
 * Model festgelegt werden, das wir on-the-fly generieren und im Output unter
 * {@code models/block/_gen/<basename>_N.json} ablegen. Identische
 * (Model, Texture-Map) Tupel werden dedupliziert.
 *
 * Nicht-trivialer Punkt: Der Forge-Variant-Key {@code "normal"} wird zum
 * leeren Vanilla-Key {@code ""}; {@code "inventory"} wird auf das passende
 * Item-Model gemappt; alle anderen state-spezifischen Keys
 * (z.B. {@code "facing=east,half=lower,..."}) bleiben unveraendert -- die
 * sind in 1.14.4 schon valides Format.
 */
class ForgeMarkerBlockstateStep extends AssetMigrationStep {

    ForgeMarkerBlockstateStep() {
        super('forge-marker-blockstate')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/blockstates\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final def parsed = new JsonSlurper().parse(file)
        if (!(parsed instanceof Map)) {
            return
        }
        final Map json = (Map) parsed
        if (json.forge_marker != 1) {
            return
        }

        final String[] segs = relPath.split('/')
        final String modid = segs[1]
        final String stateName = file.name.replaceAll(/\.json$/, '')

        final Map defaults = (json.defaults instanceof Map) ? (Map) json.defaults : [:]
        final Map variants = (json.variants instanceof Map) ? (Map) json.variants : [:]

        final Map<String, String> dedup = [:]
        int counter = 0
        final Map<String, Object> newVariants = [:]
        Map itemModelData = null

        for (final Map.Entry vent : variants.entrySet()) {
            final String key = vent.key as String
            final Object rawValue = vent.value
            // Variant-Wert: Liste (Multi-model-Pick im 1.12-Format) oder einzelnes Map.
            final Map variantData
            if (rawValue instanceof List && !((List) rawValue).isEmpty()) {
                variantData = (Map) ((List) rawValue)[0]
            } else if (rawValue instanceof Map) {
                variantData = (Map) rawValue
            } else {
                variantData = [:]
            }

            final String modelRef = (variantData.model ?: defaults.model) as String
            final Map textures = mergeTextures(
                    defaults.textures instanceof Map ? (Map) defaults.textures : null,
                    variantData.textures instanceof Map ? (Map) variantData.textures : null)

            final String dedupKey = "${modelRef}|${textures}"
            String genName = dedup.get(dedupKey)
            if (genName == null) {
                genName = "_gen/${stateName}_${counter++}"
                dedup.put(dedupKey, genName)
                final String childModel = generateChildModel(modelRef, textures)
                ctx.write("assets/${modid}/models/block/${genName}.json", childModel)
            }

            final Map<String, Object> newVariant = [:]
            newVariant.model = "${modid}:block/${genName}".toString()
            if (variantData.x != null) newVariant.x = variantData.x
            if (variantData.y != null) newVariant.y = variantData.y
            final Object uvlock = variantData.uvlock != null ? variantData.uvlock : defaults.uvlock
            if (uvlock != null) newVariant.uvlock = (Boolean) uvlock

            if (key == 'normal') {
                newVariants[''] = newVariant
            } else if (key == 'inventory') {
                itemModelData = [parent: "${modid}:block/${genName}".toString()]
            } else {
                newVariants[key] = newVariant
            }
        }

        // Falls keine non-inventory variants uebrig (nur "inventory" definiert),
        // muessen wir trotzdem einen leeren Default schreiben sonst registriert
        // MC den Block nicht.
        if (newVariants.isEmpty() && itemModelData) {
            newVariants[''] = [model: itemModelData.parent]
        }

        ctx.write(relPath, JsonOutput.prettyPrint(JsonOutput.toJson([variants: newVariants])))

        if (itemModelData) {
            ctx.write("assets/${modid}/models/item/${stateName}.json",
                    JsonOutput.prettyPrint(JsonOutput.toJson(itemModelData)))
        }
    }

    private static Map mergeTextures(final Map a, final Map b) {
        final Map result = [:]
        if (a) {
            a.each { k, v -> result.put(stripHash(k as String), v) }
        }
        if (b) {
            b.each { k, v -> result.put(stripHash(k as String), v) }
        }
        return result
    }

    private static String stripHash(final String key) {
        return key.startsWith('#') ? key.substring(1) : key
    }

    private static String generateChildModel(final String parentRef, final Map textures) {
        final Map model = [:]
        model.parent = ensureBlockPrefix(parentRef)
        if (textures && !textures.isEmpty()) {
            model.textures = textures
        }
        return JsonOutput.prettyPrint(JsonOutput.toJson(model))
    }

    /**
     * 1.12.2 erlaubte Modelreferenzen ohne {@code block/} prefix
     * ({@code "tcutility:cube"}); 1.14.4 verlangt explizit den Sub-Folder.
     */
    private static String ensureBlockPrefix(final String ref) {
        if (!ref) return ref
        final int colon = ref.indexOf(':')
        final String namespace = colon >= 0 ? ref.substring(0, colon) : null
        final String path = colon >= 0 ? ref.substring(colon + 1) : ref
        if (path.contains('/')) return ref
        return namespace ? "${namespace}:block/${path}" : "block/${path}"
    }
}
