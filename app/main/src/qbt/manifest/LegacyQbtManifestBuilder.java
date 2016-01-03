package qbt.manifest;

import qbt.VcsVersionDigest;
import qbt.tip.RepoTip;

public interface LegacyQbtManifestBuilder<M extends LegacyQbtManifest<M, B>, B extends LegacyQbtManifestBuilder<M, B>> {
    B withRepoVersion(RepoTip repo, VcsVersionDigest commit);
    B withoutRepo(RepoTip repo);
    M build();
}
