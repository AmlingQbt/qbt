package qbt.repo;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.config.QbtConfig;

public final class Repos {
    private Repos() {
        // nope
    }

    public CommonRepoAccessor findCommon(QbtConfig config, PackageTip repo, VcsVersionDigest version) {
    }
}
