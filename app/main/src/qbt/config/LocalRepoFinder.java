package qbt.config;

import java.nio.file.Path;
import qbt.vcs.LocalVcs;

public interface LocalRepoFinder {
    public Pair<Path, LocalVcs> findRepo(PackageTip repo);
}
