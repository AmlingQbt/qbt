package qbt.mains;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Collection;
import misc1.commons.Maybe;
import misc1.commons.options.NamedStringSingletonArgumentOptionsFragment;
import misc1.commons.options.OptionsFragment;
import misc1.commons.options.OptionsResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qbt.HelpTier;
import qbt.PackageTip;
import qbt.QbtCommand;
import qbt.QbtCommandName;
import qbt.QbtCommandOptions;
import qbt.QbtManifest;
import qbt.RepoManifest;
import qbt.VcsVersionDigest;
import qbt.config.QbtConfig;
import qbt.options.ConfigOptionsDelegate;
import qbt.options.ManifestOptionsDelegate;
import qbt.options.RepoActionOptionsDelegate;
import qbt.remote.QbtRemote;
import qbt.repo.PinnedRepoAccessor;
import qbt.vcs.RawRemote;

public class PushPins extends QbtCommand<PushPins.Options> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushPins.class);

    @QbtCommandName("pushPins")
    public static interface Options extends QbtCommandOptions {
        public static final ConfigOptionsDelegate<Options> config = new ConfigOptionsDelegate<Options>();
        public static final ManifestOptionsDelegate<Options> manifest = new ManifestOptionsDelegate<Options>();
        public static final RepoActionOptionsDelegate<Options> repos = new RepoActionOptionsDelegate<Options>(RepoActionOptionsDelegate.NoArgsBehaviour.OVERRIDES);
        public static final OptionsFragment<Options, ?, String> remote = new NamedStringSingletonArgumentOptionsFragment<Options>(ImmutableList.of("--remote"), Maybe.<String>not(), "QBT remote to which to push");
    }

    @Override
    public Class<Options> getOptionsClass() {
        return Options.class;
    }

    @Override
    public HelpTier getHelpTier() {
        return HelpTier.COMMON;
    }

    @Override
    public String getDescription() {
        return "Push pins to remote qbt repositories";
    }

    @Override
    public int run(OptionsResults<? extends Options> options) throws IOException {
        QbtConfig config = Options.config.getConfig(options);
        QbtManifest manifest = Options.manifest.getResult(options).parse();
        Collection<PackageTip> repos = Options.repos.getRepos(config, manifest, options);
        String qbtRemoteString = options.get(Options.remote);
        QbtRemote qbtRemote = config.qbtRemoteFinder.requireQbtRemote(qbtRemoteString);
        int total = 0;
        for(PackageTip repo : repos) {
            RepoManifest repoManifest = manifest.repos.get(repo);
            if(repoManifest == null) {
                throw new IllegalArgumentException("No such repo [tip]: " + repo);
            }
            VcsVersionDigest version = repoManifest.version;
            PinnedRepoAccessor pinnedAccessor = config.localPinsRepo.requirePin(repo, version);
            RawRemote remote = qbtRemote.requireRemote(repo);

            pinnedAccessor.pushToRemote(remote);
        }
        return 0;
    }
}
