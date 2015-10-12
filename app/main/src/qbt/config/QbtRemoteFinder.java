package qbt.config;

import qbt.repo.QbtRemote;

public interface QbtRemoteFinder {
    public QbtRemote findQbtRemote(String remote);
}
