package qbt.config;

import com.google.common.collect.ImmutableList;
import qbt.PackageTip;
import qbt.VcsVersionDigest;
import qbt.repo.CommonRepoAccessor;
import qbt.repo.LocalRepoAccessor;
import qbt.repo.RemoteRepoAccessor;

public final class RepoConfigEntry {
    public final LocalRepoConfigEntry localConfig;
    public final ImmutableList<RemoteRepoConfigEntry> remoteConfigs;

    public RepoConfigEntry(LocalRepoConfigEntry localConfig, ImmutableList<RemoteRepoConfigEntry> remoteConfigs) {
        this.localConfig = localConfig;
        this.remoteConfigs = remoteConfigs;
    }

    public CommonRepoAccessor findRepo(PackageTip repo, VcsVersionDigest version) {
        if(localConfig != null) {
            CommonRepoAccessor r = localConfig.findRepo(repo, version);
            if(r != null) {
                return r;
            }
        }

        for(RemoteRepoConfigEntry remoteConfig : remoteConfigs) {
            CommonRepoAccessor r = remoteConfig.findRepo(repo, version);
            if(r != null) {
                return r;
            }
        }

        return null;
    }

    public RemoteRepoAccessor findRepoRemote(PackageTip repo, VcsVersionDigest version) {
        for(RemoteRepoConfigEntry remoteConfig : remoteConfigs) {
            RemoteRepoAccessor r = remoteConfig.findRepoRemote(repo, version);
            if(r != null) {
                return r;
            }
        }

        return null;
    }

    public LocalRepoAccessor findRepoLocal(PackageTip repo) {
        if(localConfig != null) {
            return localConfig.findRepoLocal(repo);
        }
        return null;
    }

    public LocalRepoAccessor createLocalRepo(PackageTip repo) {
        if(localConfig != null) {
            return localConfig.createLocalRepo(repo);
        }
        return null;
    }
}
