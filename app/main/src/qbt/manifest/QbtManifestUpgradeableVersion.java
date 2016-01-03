package qbt.manifest;

abstract class QbtManifestUpgradeableVersion<M extends LegacyQbtManifest<M, B> & UpgradeableQbtManifest<N>, B extends LegacyQbtManifestBuilder<M, B>, N extends LegacyQbtManifest<N, ?>> extends QbtManifestVersion<M, B> {
    public final Class<N> upgradeManifestClass;

    QbtManifestUpgradeableVersion(int version, Class<M> manifestClass, Class<N> upgradeManifestClass) {
        super(version, manifestClass);
        this.upgradeManifestClass = upgradeManifestClass;
    }
}
