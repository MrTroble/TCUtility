package com.troblecodings.build

import groovy.transform.CompileStatic

/**
 * Runs a configurable list of {@link AssetMigrationStep}s in order over a
 * directory of resource files.
 *
 * The pipeline is purposefully version-agnostic: a step decides itself if it
 * applies to a given file (via its {@code matches} predicate). New
 * Minecraft-version migrations are added by registering a fresh
 * {@code List<AssetMigrationStep>} on {@link AssetMigrations}; the pipeline
 * itself never has to change.
 *
 * Steps run sequentially so a later step always sees the output of earlier
 * ones — this lets us express dependencies like "rewrite the texture refs
 * after the blockstate has been converted to standard format".
 */
class AssetMigrationPipeline {

    private final List<AssetMigrationStep> steps = []
    private final List<String> reportLines = []

    AssetMigrationPipeline addStep(AssetMigrationStep step) {
        steps.add(step)
        return this
    }

    AssetMigrationPipeline addAll(List<AssetMigrationStep> stepsToAdd) {
        steps.addAll(stepsToAdd)
        return this
    }

    List<String> getReport() { return reportLines }

    /**
     * Run all configured steps over {@code root} in place.
     *
     * Each step gets a fresh {@link MigrationContext} that exposes the
     * current file tree as well as helpers for rewriting / adding / removing
     * files. Mutations are applied to disk after the step returns.
     */
    void run(File root) {
        reportLines.clear()
        if (!root.exists() || !root.isDirectory()) {
            return
        }
        for (AssetMigrationStep step : steps) {
            final MigrationContext ctx = new MigrationContext(root, reportLines, step.name)
            // Snapshot the file list so the step can add new files without
            // disturbing iteration order.
            final List<File> files = []
            root.eachFileRecurse { File f ->
                if (f.isFile()) {
                    files.add(f)
                }
            }
            for (File f : files) {
                final String rel = root.toPath().relativize(f.toPath()).toString().replace('\\', '/')
                if (step.matches(rel)) {
                    step.apply(ctx, rel, f)
                }
            }
            ctx.flush()
        }
    }
}
