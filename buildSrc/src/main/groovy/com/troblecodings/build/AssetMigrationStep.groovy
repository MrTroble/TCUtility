package com.troblecodings.build

/**
 * One unit of work in an asset migration pipeline. A step decides if it
 * applies to a given file via its {@link #matches(String)} predicate; the
 * pipeline then calls {@link #apply(MigrationContext, String, File)} with
 * the matching file. The step is free to read and mutate the file as well
 * as register additional file writes / deletions through the
 * {@link MigrationContext}.
 *
 * Steps are intentionally small and composable. To support a new Minecraft
 * version transition, register a new list of steps in
 * {@link AssetMigrations}; existing steps remain reusable as long as they
 * are still valid for the new transition.
 */
abstract class AssetMigrationStep {

    /** Display name used for build log lines. */
    final String name

    AssetMigrationStep(final String name) {
        this.name = name
    }

    /**
     * @param relPath relative path from the resource root using forward
     *                slashes, e.g. {@code "assets/tcutility/blockstates/foo.json"}
     */
    abstract boolean matches(String relPath)

    /**
     * Process a single matched file. The step may mutate {@code file} in
     * place or use {@code ctx.write/remove/rename} for queued mutations
     * (which run after the step returns).
     */
    abstract void apply(MigrationContext ctx, String relPath, File file)
}
