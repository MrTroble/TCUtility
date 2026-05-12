package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext

/**
 * Forge hat in 1.20 den Model-Loader fuer DynamicBucketModel von {@code forge:bucket} auf
 * {@code forge:fluid_container} umbenannt. {@link FluidAssetGenStep} schreibt die Bucket-Models
 * weiterhin im 1.14-Stil mit dem alten Loader -- dieser Step rewritet die generierten Files
 * (und alle anderen *_bucket-Models) nachgelagert auf den neuen Loader-Namen.
 *
 * Aelteren Versionen (1.14 .. 1.19) ist der Step egal, weil die Pipeline ihn nur fuer
 * 1.20+-Builds einhaengt.
 */
class BucketLoaderRenameStep extends AssetMigrationStep {

    BucketLoaderRenameStep() {
        super('bucket-loader-rename')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/models\/item\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String original = file.text
        final String rewritten = original.replace('"forge:bucket"', '"forge:fluid_container"')
        if (rewritten != original) {
            ctx.write(relPath, rewritten)
        }
    }
}
