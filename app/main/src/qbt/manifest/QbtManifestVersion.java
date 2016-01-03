package qbt.manifest;

import misc1.commons.merge.Merge;

public abstract class QbtManifestVersion<M extends LegacyQbtManifest<M, B>, B extends LegacyQbtManifestBuilder<M, B>> {
    final int version;
    Class<M> manifestClass;

    QbtManifestVersion(int version, Class<M> manifestClass) {
        this.version = version;
        this.manifestClass = manifestClass;
    }

    public M upgrade(LegacyQbtManifest<?, ?> manifest) {
        while(true) {
            if(manifestClass.isInstance(manifest)) {
                return manifestClass.cast(manifest);
            }
            if(manifest instanceof UpgradeableQbtManifest) {
                manifest = ((UpgradeableQbtManifest<?>) manifest).upgrade();
                continue;
            }
            throw new IllegalArgumentException();
        }
    }

    public QbtManifestVersion<?, ?> max(QbtManifestVersion<?, ?> other) {
        if(other.version > version) {
            return other;
        }
        return this;
    }

    public abstract Merge<M> merge();
    public abstract QbtManifestParser<M> parser();
}
