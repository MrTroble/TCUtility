package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext

/**
 * Vanilla-Texture-Renames, die nach dem 1.12-zu-1.14-Flattening passieren -- also Refs der Form
 * {@code minecraft:block/X} (Singular). {@link VanillaTextureRefStep} kuemmert sich um den
 * 1.12-Plural-Pfad und die bekannten Asset-Renames bis 1.14; spaetere Renames (zB. 1.17:
 * {@code grass_path_top} -> {@code dirt_path_top}) muessen auf den bereits singularisierten
 * Refs operieren und kommen daher in einem separaten Step.
 *
 * Eintraege gelten fuer 1.17+ (1.16.5 hat noch die alten Namen). Daher haengt der Build dieses
 * Step an die {@code mc1_16_to_mc1_19}-Pipeline an, sodass alle 1.19+-Builds die Renames
 * mitnehmen.
 */
class VanillaPostFlatteningRenameStep extends AssetMigrationStep {

    /** Vanilla-Texture-Name (Singular) -> 1.17+-Name. */
    private static final Map<String, String> RENAMES = [
            'block/grass_path_top' : 'block/dirt_path_top',
            'block/grass_path_side': 'block/dirt_path_side',
    ]

    VanillaPostFlatteningRenameStep() {
        super('vanilla-post-flattening-rename')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/(?:blockstates|models)\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String original = file.text
        String rewritten = original
        RENAMES.each { from, to ->
            rewritten = rewritten.replace('"minecraft:' + from + '"', '"minecraft:' + to + '"')
        }
        if (rewritten != original) {
            ctx.write(relPath, rewritten)
        }
    }
}
