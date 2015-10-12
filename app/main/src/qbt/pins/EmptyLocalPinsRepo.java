package qbt.pins;

import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.repo.RemoteRepoAccessor;

public final class EmptyLocalPinsRepo extends AbstractLocalPinsRepo {
    @Override
    public RemoteRepoAccessor findPin(PackageTip repo, VcsVersionDigest version) {
        return null;
    }
}
