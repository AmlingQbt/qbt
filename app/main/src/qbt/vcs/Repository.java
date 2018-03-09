package qbt.vcs;

import com.google.common.collect.Multimap;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import qbt.VcsTreeDigest;
import qbt.VcsVersionDigest;

public interface Repository {
    // Various not-very-horrible informational stuff
    public VcsTreeDigest getEffectiveTree(String subpath);
    public boolean isAncestorOf(VcsVersionDigest ancestor, VcsVersionDigest descendent);
    public VcsVersionDigest getCurrentCommit();
    public Iterable<String> showFile(VcsVersionDigest commit, String path);
    public Iterable<String> showFile(VcsTreeDigest commit, String path);
    public boolean commitExists(VcsVersionDigest version);
    public Multimap<String, String> getAllConfig();
    public boolean isClean();
    public boolean isClean(CommitLevel level);
    public Path getRoot();
    public VcsTreeDigest getSubtree(VcsVersionDigest version, String subpath);
    public VcsTreeDigest getSubtree(VcsTreeDigest tree, String subpath);
    public Map<VcsVersionDigest, CommitData> revWalk(Collection<VcsVersionDigest> from, Collection<VcsVersionDigest> to);
    public CommitData getCommitData(VcsVersionDigest commit);
    public VcsVersionDigest getUserSpecifiedCommit(String arg);
    public List<String> getUserVisibleStatus();

    // Various semi-crummy mutators invented for various semi-crummy needs
    public void checkout(VcsVersionDigest version);
    public void checkoutTree(VcsTreeDigest tree, Path dest);
    public VcsVersionDigest commit(boolean amend, String message, CommitLevel level);
    public VcsTreeDigest getIndexTree();
    public void setIndexTree(VcsTreeDigest tree);

    // before: repo is somewhere, clean
    // after: [failure] throw, repo state unclear
    // after: [success] repo is at merge of previous place and commit, clean
    public void merge(VcsVersionDigest commit);

    // before: repo is somewhere, clean
    // after: [failure] throw, repo state unclear
    // after: [success] repo is at rebuild on previous place of [linear] from..to, clean
    public void rebase(VcsVersionDigest from, VcsVersionDigest to);

    // create commit in the background
    public VcsVersionDigest createCommit(CommitData commitData);

    // operations on a tree in this repo
    public TreeAccessor getTreeAccessor(VcsTreeDigest tree);
    public TreeAccessor getEmptyTreeAccessor();
}
