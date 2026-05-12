package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * 1.12.2 Forge-Blockstates erlaubten Texture-Variablen mit fuehrendem
 * {@code #} im {@code textures}-Block (z.B. {@code "#texture": "#texture"}).
 * Vanilla 1.13+ akzeptiert das nicht mehr - der Schluessel ist der Variablen-
 * name <em>ohne</em> {@code #}, das {@code #} bleibt nur im Wert um auf eine
 * andere Texture-Variable zu verweisen.
 */
class FixHashedTextureKeysStep extends AssetMigrationStep {

    FixHashedTextureKeysStep() {
        super('fix-hashed-texture-keys')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/models\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final def json = new JsonSlurper().parse(file)
        if (!(json instanceof Map)) {
            return
        }
        final Object textures = json.textures
        if (!(textures instanceof Map)) {
            return
        }
        boolean changed = false
        final Map<String, Object> rewritten = [:]
        ((Map) textures).each { k, v ->
            final String key = k as String
            if (key.startsWith('#')) {
                rewritten[key.substring(1)] = v
                changed = true
            } else {
                rewritten[key] = v
            }
        }
        if (changed) {
            json.textures = rewritten
            ctx.write(relPath, JsonOutput.prettyPrint(JsonOutput.toJson(json)))
        }
    }
}
