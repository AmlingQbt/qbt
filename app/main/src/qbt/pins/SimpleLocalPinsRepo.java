package qbt.pins;

import com.google.common.base.Function;
import java.nio.file.Path;
import org.apache.commons.lang3.ObjectUtils;
import qbt.PackageTip;
import qbt.QbtUtils;
import qbt.VcsVersionDigest;
import qbt.repo.PinnedRepoAccessor;
import qbt.vcs.RawRemoteVcs;

public final class SimpleLocalPinsRepo extends AbstractLocalPinsRepo {
    private final RawRemoteVcs vcs;
    private final Path root;

    public SimpleLocalPinsRepo(RawRemoteVcs vcs, Path root) {
        this.vcs = vcs;
        this.root = root;
    }

    @Override
    public PinnedRepoAccessor findPin(PackageTip repo, VcsVersionDigest version) {
        Path cache = root.resolve(repo.pkg);

        QbtUtils.semiAtomicDirCache(cache, "", new Function<Path, ObjectUtils.Null>() {
            @Override
            public ObjectUtils.Null apply(Path cacheTemp) {
                vcs.getLocalVcs().createCacheRepo(cacheTemp);
                return ObjectUtils.NULL;
            }
        });

        if(!vcs.getLocalVcs().getRepository(cache).commitExists(version)) {
            return null;
        }

        return new PinnedRepoAccessor(vcs, cache, version);
    }
}
