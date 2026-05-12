package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Sammelt aus den blockdefinitions-JSONs alle Eintraege mit den States
 * {@code fence} bzw. {@code wall} und schreibt:
 *
 * <ul>
 *   <li>{@code data/<modid>/tags/blocks/fences.json} mit allen
 *       {@code fence_<key>}-Block-Refs des Mods</li>
 *   <li>{@code data/<modid>/tags/blocks/walls.json} mit allen
 *       {@code wall_<key>}-Block-Refs des Mods</li>
 *   <li>Spiegel-Files unter {@code data/<modid>/tags/items/} fuer die
 *       Item-Pendants</li>
 *   <li>{@code data/minecraft/tags/blocks/fences.json} und
 *       {@code .../walls.json}, die als Member den Mod-Tag {@code #<modid>:fences}
 *       bzw. {@code #<modid>:walls} aufnehmen, damit Vanilla-Fences und
 *       -Walls die unsrigen als gleichwertig erkennen und Connection-
 *       Verhalten greift</li>
 * </ul>
 *
 * Ohne diese Tags verbinden sich TCFence- bzw. TCWall-Blocks in 1.13+
 * weder untereinander noch mit Vanilla -- vanilla pruefen alle Connections
 * via {@code BlockTags.FENCES} / {@code BlockTags.WALLS}.
 */
class FenceWallTagGenStep extends AssetMigrationStep {

    FenceWallTagGenStep() {
        super('fence-wall-tag-gen')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/blockdefinitions\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String modid = relPath.split('/')[1]
        final List<String> fenceIds = collectIds(ctx, modid, 'fences')
        final List<String> wallIds = collectIds(ctx, modid, 'walls')

        final def parsed = new JsonSlurper().parse(file)
        if (!(parsed instanceof Map)) {
            return
        }
        ((Map) parsed).each { key, def value ->
            if (!(value instanceof Map)) {
                return
            }
            final List states = (value.states instanceof List) ? (List) value.states : []
            for (final Object stateRaw : states) {
                final String state = String.valueOf(stateRaw)
                if (state == 'fence') {
                    fenceIds.add("${modid}:fence_${key}".toString())
                } else if (state == 'wall') {
                    wallIds.add("${modid}:wall_${key}".toString())
                }
            }
        }
        // Re-write akkumulierten Stand. Spaetere Aufrufe (anderes JSON-File
        // im selben Verzeichnis) ergaenzen, weil Pipelines pro File matchen.
        writeAccumulatedTags(ctx, modid, fenceIds, wallIds)
    }

    private static List<String> collectIds(final MigrationContext ctx, final String modid,
            final String tagName) {
        final File existing = ctx.resolve("data/${modid}/tags/blocks/${tagName}.json")
        if (!existing.isFile()) {
            return new ArrayList<String>()
        }
        try {
            final def parsed = new JsonSlurper().parse(existing)
            if (parsed instanceof Map && parsed.values instanceof List) {
                return new ArrayList<String>(((List) parsed.values).collect { it as String })
            }
        } catch (final Exception ignored) {
            // korrupte oder fremde Datei -- starten neu
        }
        return new ArrayList<String>()
    }

    private static void writeAccumulatedTags(final MigrationContext ctx, final String modid,
            final List<String> fenceIds, final List<String> wallIds) {
        if (!fenceIds.isEmpty()) {
            final List<String> distinctFences = fenceIds.unique(false) as List<String>
            ctx.write("data/${modid}/tags/blocks/fences.json", tagJson(distinctFences))
            ctx.write("data/${modid}/tags/items/fences.json", tagJson(distinctFences))
            ctx.write("data/minecraft/tags/blocks/fences.json",
                    tagJson(["#${modid}:fences".toString()]))
            ctx.write("data/minecraft/tags/items/fences.json",
                    tagJson(["#${modid}:fences".toString()]))
        }
        if (!wallIds.isEmpty()) {
            final List<String> distinctWalls = wallIds.unique(false) as List<String>
            ctx.write("data/${modid}/tags/blocks/walls.json", tagJson(distinctWalls))
            ctx.write("data/${modid}/tags/items/walls.json", tagJson(distinctWalls))
            ctx.write("data/minecraft/tags/blocks/walls.json",
                    tagJson(["#${modid}:walls".toString()]))
            ctx.write("data/minecraft/tags/items/walls.json",
                    tagJson(["#${modid}:walls".toString()]))
        }
    }

    private static String tagJson(final List<String> values) {
        return JsonOutput.prettyPrint(JsonOutput.toJson([replace: false, values: values]))
    }
}
