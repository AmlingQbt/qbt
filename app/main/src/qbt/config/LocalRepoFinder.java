package qbt.config;

import java.nio.file.Path;
import qbt.vcs.LocalVcs;
import org.apache.commons.lang3.tuple.Pair;
import qbt.PackageTip;

public interface LocalRepoFinder {
    public Pair<Path, LocalVcs> findRepo(PackageTip repo);
}
