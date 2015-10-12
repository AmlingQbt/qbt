package qbt.repo;

import qbt.PackageDirectory;

public interface CommonRepoAccessor {
    public PackageDirectory makePackageDirectory(String prefix);
}
