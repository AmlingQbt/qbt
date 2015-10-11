package qbt.diffmanifests;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import qbt.QbtTempDir;
import qbt.VcsVersionDigest;
import qbt.utils.ProcessHelper;
import qbt.vcs.CachedRemote;
import qbt.vcs.LocalVcs;

public abstract class Runner {
    private Runner() {
    }

    public abstract Runner addEnv(String key, String value);
    public abstract Runner findCommit(CachedRemote remote, VcsVersionDigest version);
    public abstract Runner checkout(VcsVersionDigest version);
    public abstract void run();

    private static final Runner DEAD_RUNNER = new Runner() {
        @Override
        public Runner addEnv(String key, String value) {
            return this;
        }

        @Override
        public Runner findCommit(CachedRemote remote, VcsVersionDigest version) {
            return this;
        }

        @Override
        public Runner checkout(VcsVersionDigest version) {
            return this;
        }

        @Override
        public void run() {
        }
    };

    public static Runner dead() {
        return DEAD_RUNNER;
    }

    private static class RealRunner extends Runner {
        private final String prefix;
        private final QbtTempDir dir;
        private ProcessHelper p;
        private LocalVcs localVcs;

        public RealRunner(String prefix, String command) {
            this.prefix = prefix;
            this.dir = new QbtTempDir();
            this.p = new ProcessHelper(dir.path, "sh", "-c", command);
            this.localVcs = null;
        }

        @Override
        public Runner addEnv(String key, String value) {
            p = p.putEnv(key, value);
            return this;
        }

        @Override
        public Runner findCommit(CachedRemote remote, VcsVersionDigest version) {
            if(localVcs == null) {
                localVcs = remote.getLocalVcs();
                localVcs.createWorkingRepo(dir.path);
            }
            else {
                if(!localVcs.equals(remote.getLocalVcs())) {
                    throw new RuntimeException("Mismatched local VCSs: " + localVcs + " / " + remote.getLocalVcs());
                }
            }
            remote.findCommit(dir.path, ImmutableList.of(version));
            return this;
        }

        @Override
        public Runner checkout(VcsVersionDigest version) {
            if(localVcs == null) {
                throw new RuntimeException("Runner.checkout() called w/no localVcs");
            }
            localVcs.getRepository(dir.path).checkout(version);
            return this;
        }

        @Override
        public void run() {
            try {
                if(prefix != null) {
                    p = p.combineError();
                    p.completeLinesCallback(new Function<String, Void>() {
                        @Override
                        public Void apply(String line) {
                            System.out.println("[" + prefix + "] " + line);
                            return null;
                        }
                    });
                }
                else {
                    p = p.inheritOutput();
                    p = p.inheritError();
                    p.completeVoid();
                }
            }
            finally {
                // This could be slightly more finally (we could avoid creating
                // dir until run()), but oh well.
                dir.close();
            }
        }
    }

    public static Runner real(String prefix, String command) {
        return new RealRunner(prefix, command);
    }
}
