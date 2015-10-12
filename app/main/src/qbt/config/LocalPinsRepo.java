package qbt.config;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.repo.PinnedRepoAccessor;

public interface LocalPinsRepo {
    public PinnedRepoAccessor findPin(PackageTip repo, VcsVersionDigest version);
    public PinnedRepoAccessor requirePin(PackageTip repo, VcsVersionDigest version);
}
