package qbt.manifest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import misc1.commons.merge.Merge;
import misc1.commons.merge.Merges;
import qbt.VcsVersionDigest;
import qbt.manifest.current.QbtManifest;
import qbt.manifest.current.RepoManifest;
import qbt.tip.RepoTip;

class V1QbtManifestVersion extends QbtManifestVersion<V1QbtManifestVersion.Manifest, V1QbtManifestVersion.Builder> {
    public V1QbtManifestVersion() {
        super(1, Manifest.class);
    }

    class Manifest implements LegacyQbtManifest<Manifest, Builder> {
        public final QbtManifest manifest;

        public Manifest(QbtManifest manifest) {
            this.manifest = manifest;
        }

        @Override
        public ImmutableSet<RepoTip> getRepos() {
            return manifest.repos.keySet();
        }

        @Override
        public ImmutableList<String> deparse() {
            return parser().deparse(this);
        }

        @Override
        public Builder builder() {
            return new Builder(manifest.builder());
        }

        @Override
        public QbtManifest current() {
            return manifest;
        }
    }

    class Builder implements LegacyQbtManifestBuilder<Manifest, Builder> {
        public QbtManifest.Builder builder;

        public Builder(QbtManifest.Builder builder) {
            this.builder = builder;
        }

        @Override
        public Builder withRepoVersion(RepoTip repo, VcsVersionDigest commit) {
            return new Builder(builder.with(repo, builder.get(repo).set(RepoManifest.VERSION, commit)));
        }

        @Override
        public Builder withoutRepo(RepoTip repo) {
            return new Builder(builder.without(repo));
        }

        @Override
        public Manifest build() {
            return new Manifest(builder.build());
        }
    }

    @Override
    public Merge<Manifest> merge() {
        return Merges.wrapper((manifest) -> manifest.manifest, QbtManifest.TYPE.merge(), Manifest::new);
    }

    @Override
    public QbtManifestParser<Manifest> parser() {
        return new JsonQbtManifestParser<Manifest, Builder>(this) {
            @Override
            protected JsonSerializer<Builder> serializer() {
                return JsonSerializers.wrapper((builder) -> builder.builder, QbtManifest.SERIALIZER, Builder::new);
            }
        };
    }
}
