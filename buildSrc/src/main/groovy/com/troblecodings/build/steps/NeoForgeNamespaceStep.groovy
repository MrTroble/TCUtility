package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext

/**
 * NeoForge 1.20.5+ hat den Forge-Namespace aus Model-Loadern und Builtin-Modellen herausgezogen
 * und durch {@code neoforge:} ersetzt. Der von {@link FluidAssetGenStep} generierte Bucket-
 * Model-Loader heisst seit 1.20.4 {@code forge:fluid_container} (siehe BucketLoaderRenameStep)
 * -- in NeoForge 1.21 muss daraus {@code neoforge:fluid_container} werden, sonst kann der Model-
 * Loader nicht aufgeloest werden und Buckets verlieren ihre Textur.
 *
 * Auch {@code parent: "forge:item/bucket"} (das builtin Bucket-Model) wird auf
 * {@code parent: "neoforge:item/bucket"} umgeschrieben, weil das Model-Asset in NeoForge unter
 * dem neoforge-Namespace liegt.
 */
class NeoForgeNamespaceStep extends AssetMigrationStep {

    NeoForgeNamespaceStep() {
        super('neoforge-namespace')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/models\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String original = file.text
        String rewritten = original
                .replace('"forge:fluid_container"', '"neoforge:fluid_container"')
                .replace('"forge:item/bucket"', '"neoforge:item/bucket"')
        if (rewritten != original) {
            ctx.write(relPath, rewritten)
        }
    }
}
