package qbt.manifest;

public interface UpgradeableQbtManifest<N extends LegacyQbtManifest<N, ?>> {
    N upgrade();
}
