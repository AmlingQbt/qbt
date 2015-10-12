package qbt.config;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.repo.RemoteRepoAccessor;

public interface LocalPinsRepo {
    public RemoteRepoAccessor findPin(PackageTip repo, VcsVersionDigest version);
    public RemoteRepoAccessor requirePin(PackageTip repo, VcsVersionDigest version);
}
