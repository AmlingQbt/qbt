package qbt.pins;

import com.google.common.base.Function;
import java.nio.file.Path;
import org.apache.commons.lang3.ObjectUtils;
import qbt.PackageTip;
import qbt.QbtUtils;
import qbt.VcsVersionDigest;
import qbt.repo.PinnedRepoAccessor;
import qbt.vcs.CacheDirCachedRemoteVcs;
import qbt.vcs.CachedRemote;
import qbt.vcs.CachedRemoteVcs;
import qbt.vcs.RawRemoteVcs;

public final class SimpleLocalPinsRepo extends AbstractLocalPinsRepo {
    private final CachedRemoteVcs vcs;
    private final Path root;

    public SimpleLocalPinsRepo(RawRemoteVcs rawRemoteVcs, Path root) {
        this.vcs = new CacheDirCachedRemoteVcs(rawRemoteVcs, root.resolve("remotes"));
        this.root = root;
    }

    @Override
    public PinnedRepoAccessor findPin(PackageTip repo, VcsVersionDigest version) {
        Path cache = root.resolve("pins").resolve(repo.pkg);

        QbtUtils.semiAtomicDirCache(cache, "", new Function<Path, ObjectUtils.Null>() {
            @Override
            public ObjectUtils.Null apply(Path cacheTemp) {
                vcs.getRawRemoteVcs().getLocalVcs().createCacheRepo(cacheTemp);
                return ObjectUtils.NULL;
            }
        });

        if(!vcs.getRawRemoteVcs().getLocalVcs().getRepository(cache).commitExists(version)) {
            return null;
        }

        return new PinnedRepoAccessor(new CachedRemote(cache.toAbsolutePath().toString(), vcs), version);
    }
}
