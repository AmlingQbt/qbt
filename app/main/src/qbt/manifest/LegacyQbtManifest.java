package qbt.manifest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import qbt.manifest.current.QbtManifest;
import qbt.tip.RepoTip;

public interface LegacyQbtManifest<M extends LegacyQbtManifest<M, B>, B extends LegacyQbtManifestBuilder<M, B>> {
    ImmutableSet<RepoTip> getRepos();
    ImmutableList<String> deparse();
    B builder();
    QbtManifest current();
}
