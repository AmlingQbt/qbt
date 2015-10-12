package qbt.config;

import qbt.remote.QbtRemote;

public class EmptyQbtRemoteFinder implements QbtRemoteFinder {
    public QbtRemote findQbtRemote(String remote) {
        return null;
    }
}
