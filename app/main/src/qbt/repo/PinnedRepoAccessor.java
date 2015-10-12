package qbt.repo;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import misc1.commons.Maybe;
import qbt.PackageDirectory;
import qbt.QbtTempDir;
import qbt.VcsTreeDigest;
import qbt.VcsVersionDigest;
import qbt.repo.CommonRepoAccessor;
import qbt.vcs.CachedRemote;
import qbt.vcs.LocalVcs;

public final class PinnedRepoAccessor implements CommonRepoAccessor {
    private final CachedRemote remote;
    private final VcsVersionDigest version;

    public PinnedRepoAccessor(CachedRemote remote, VcsVersionDigest version) {
        this.remote = remote;
        this.version = version;
    }

    @Override
    public PackageDirectory makePackageDirectory(String prefix) {
        final QbtTempDir packageDir = new QbtTempDir();
        // We could leak packageDir if this checkout crashes but oh
        // well.
        remote.checkoutTree(version, prefix, packageDir.path);
        return new PackageDirectory() {
            @Override
            public Path getDir() {
                return packageDir.path;
            }

            @Override
            public void close() {
                packageDir.close();
            }
        };
    }

    @Override
    public VcsTreeDigest getEffectiveTree(Maybe<String> prefix) {
        if(prefix.isPresent()) {
            return remote.getSubtree(version, prefix.get(null));
        }
        else {
            return remote.getLocalVcs().emptyTree();
        }
    }

    @Override
    public boolean isOverride() {
        return false;
    }

    public void findCommit(Path dir) {
        remote.findCommit(dir, ImmutableList.of(version));
    }

    public LocalVcs getLocalVcs() {
        return remote.getLocalVcs();
    }

    public void addPin(Path dir, VcsVersionDigest version) {
        remote.addPin(dir, version);
    }

    public VcsTreeDigest getSubtree(String prefix) {
        return remote.getSubtree(version, prefix);
    }
}
