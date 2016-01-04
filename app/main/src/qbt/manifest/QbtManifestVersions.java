package qbt.manifest;

import com.google.common.collect.ImmutableList;
import java.util.List;
import qbt.manifest.current.QbtManifest;

public class QbtManifestVersions {
    private static class Internals {
        public final ImmutableList<QbtManifestVersion<?, ?>> list;

        public Internals(ImmutableList<QbtManifestVersion<?, ?>> list) {
            this.list = list;

            for(int i = 0; i < list.size(); ++i) {
                if(list.get(i).version != i) {
                    throw new IllegalArgumentException();
                }
                if(i + 1 < list.size()) {
                    QbtManifestVersion<?, ?> before = list.get(i);
                    QbtManifestVersion<?, ?> after = list.get(i + 1);
                    if(!(before instanceof QbtManifestUpgradeableVersion)) {
                        throw new IllegalArgumentException();
                    }
                    if(!((QbtManifestUpgradeableVersion<?, ?, ?>) before).upgradeManifestClass.equals(after.manifestClass)) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
    }

    static final V0QbtManifestVersion V0;
    static final V1QbtManifestVersion V1;
    static final V2QbtManifestVersion V2;
    private static final Internals INTERNALS;
    static {
        ImmutableList.Builder<QbtManifestVersion<?, ?>> b = ImmutableList.builder();

        b.add(V0 = new V0QbtManifestVersion());
        b.add(V1 = new V1QbtManifestVersion());
        b.add(V2 = new V2QbtManifestVersion());

        INTERNALS = new Internals(b.build());
    }

    public static final class LegacyParse<M extends LegacyQbtManifest<M, B>, B extends LegacyQbtManifestBuilder<M, B>> {
        public final LegacyQbtManifest<M, B> manifest;
        public final QbtManifestVersion<M, B> version;

        public LegacyParse(LegacyQbtManifest<M, B> manifest, QbtManifestVersion<M, B> version) {
            this.manifest = manifest;
            this.version = version;
        }
    }

    public static LegacyParse<?, ?> parseLegacy(List<String> lines) {
        final QbtManifestVersion<?, ?> version;
        if(lines.isEmpty()) {
            version = V0;
        }
        else {
            String line0 = lines.get(0);
            if(!line0.startsWith("@")) {
                version = V0;
            }
            else {
                lines = lines.subList(1, lines.size());
                version = INTERNALS.list.get(Integer.parseInt(line0.substring(1), 10));
            }
        }
        final List<String> linesFinal = lines;
        return new Object() {
            public <M extends LegacyQbtManifest<M, B>, B extends LegacyQbtManifestBuilder<M, B>> LegacyParse<M, B> run(QbtManifestVersion<M, B> version) {
                return new LegacyParse<M, B>(version.parser().parse(linesFinal), version);
            }
        }.run(version);
    }

    public static QbtManifest parse(List<String> lines) {
        return parseLegacy(lines).manifest.current();
    }

    public static LegacyQbtManifest<?, ?> toLegacy(QbtManifest manifest) {
        return V2.new Manifest(manifest);
    }
}
