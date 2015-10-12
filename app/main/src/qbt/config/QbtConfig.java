package qbt.config;

import groovy.lang.GroovyShell;
import java.nio.file.Path;
import misc1.commons.ExceptionUtils;
import qbt.artifactcacher.ArtifactCacher;

public final class QbtConfig {
    public final LocalRepoFinder localRepoFinder;
    public final QbtRemoteFinder qbtRemoteFinder;
    public final LocalPinsRepo localPins;
    public final ArtifactCacher artifactCacher;

    public QbtConfig(LocalRepoFinder localRepoFinder, QbtRemoteFinder qbtRemoteFinder, LocalPinsRepo localPins, ArtifactCacher artifactCacher) {
        this.localRepoFinder = localRepoFinder;
        this.qbtRemoteFinder = qbtRemoteFinder;
        this.localPins = localPins;
        this.artifactCacher = artifactCacher;
    }

    public static QbtConfig parse(Path f) {
        GroovyShell shell = new GroovyShell();
        shell.setVariable("workspaceRoot", f.getParent());
        try {
            return (QbtConfig) shell.evaluate(f.toFile());
        }
        catch(Exception e) {
            throw ExceptionUtils.commute(e);
        }
    }
}
