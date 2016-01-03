package qbt.manifest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import misc1.commons.ds.WrapperType;
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

    private final WrapperType<Manifest, QbtManifest> MANIFEST_WRAPPER_TYPE = new WrapperType<Manifest, QbtManifest>() {
        @Override
        public QbtManifest unwrap(Manifest manifest) {
            return manifest.manifest;
        }

        @Override
        public Manifest wrap(QbtManifest manifest) {
            return new Manifest(manifest);
        }
    };

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

    private final WrapperType<Builder, QbtManifest.Builder> BUILDER_WRAPPER_TYPE = new WrapperType<Builder, QbtManifest.Builder>() {
        @Override
        public QbtManifest.Builder unwrap(Builder builder) {
            return builder.builder;
        }

        @Override
        public Builder wrap(QbtManifest.Builder builder) {
            return new Builder(builder);
        }
    };

    @Override
    public Merge<Manifest> merge() {
        return Merges.wrapper(MANIFEST_WRAPPER_TYPE, QbtManifest.TYPE.merge());
    }

    @Override
    public QbtManifestParser<Manifest> parser() {
        return new JsonQbtManifestParser<Manifest, Builder>(this) {
            @Override
            protected JsonSerializer<Builder> serializer() {
                return JsonSerializers.wrapper(BUILDER_WRAPPER_TYPE, QbtManifest.SERIALIZER);
            }
        };
    }
}
