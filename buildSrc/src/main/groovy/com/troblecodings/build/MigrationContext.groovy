package com.troblecodings.build

/**
 * Provides file IO helpers and a deferred-mutation queue for an
 * {@link AssetMigrationStep}. Mutations queued during a step's
 * {@code apply} call are flushed to disk after the step finishes, so a
 * step never observes other files changing under its feet while iterating.
 */
class MigrationContext {

    private final File root
    private final List<String> log
    private final String stepName

    private final Map<String, byte[]> pendingWrites = [:]
    private final Set<String> pendingDeletes = [] as Set

    MigrationContext(final File root, final List<String> log, final String stepName) {
        this.root = root
        this.log = log
        this.stepName = stepName
    }

    File getRoot() { return root }

    /** Resolve a relative path under the migration root. */
    File resolve(final String relPath) {
        return new File(root, relPath)
    }

    /** Queue a content rewrite. Path is relative to the migration root. */
    void write(final String relPath, final String content) {
        pendingWrites[relPath] = content.getBytes('UTF-8')
        info("rewrite $relPath")
    }

    /** Queue a binary file write (e.g. for moving textures). */
    void writeBytes(final String relPath, final byte[] content) {
        pendingWrites[relPath] = content
        info("write $relPath")
    }

    /** Queue a file deletion. Path is relative to the migration root. */
    void remove(final String relPath) {
        pendingDeletes.add(relPath)
        info("remove $relPath")
    }

    /** Add a log line for the build report. */
    void info(final String message) {
        log.add("[${stepName}] ${message}".toString())
    }

    /** Apply all queued mutations to disk. Called by the pipeline. */
    void flush() {
        for (String rel : pendingDeletes) {
            final File f = new File(root, rel)
            if (f.exists()) {
                f.delete()
            }
        }
        for (Map.Entry<String, byte[]> e : pendingWrites.entrySet()) {
            final File f = new File(root, e.key)
            f.parentFile?.mkdirs()
            f.bytes = e.value
        }
        pendingDeletes.clear()
        pendingWrites.clear()
    }
}
