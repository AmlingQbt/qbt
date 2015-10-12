package qbt.config;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.repo.CommonRepoAccessor;

public interface LocalPinsRepo {
    public CommonRepoAccessor findPinnedRepo(PackageTip repo, VcsVersionDigest version);
}
