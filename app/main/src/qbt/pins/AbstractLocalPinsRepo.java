package qbt.pins;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.config.LocalPinsRepo;
import qbt.repo.RemoteRepoAccessor;

public abstract class AbstractLocalPinsRepo implements LocalPinsRepo {
    @Override
    public RemoteRepoAccessor requirePin(PackageTip repo, VcsVersionDigest version) {
        RemoteRepoAccessor r = findPin(repo, version);
        if(r == null) {
            throw new IllegalArgumentException("Could not find local pin for " + repo + " at " + version);
        }
        return r;
    }
}
