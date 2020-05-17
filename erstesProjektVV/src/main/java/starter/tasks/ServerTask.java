package starter.tasks;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;
import network.server.SyncServer;

import java.io.IOException;

/**
 * The SyncServer is started in this Thread
 */
public class ServerTask implements Runnable {
    private WatchedDirectory watchedDirectory;

    public ServerTask(WatchedDirectory watchedDirectory) {
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void run() {
        try {
            new Thread(new SyncServer(watchedDirectory)).start();
        } catch (IOException e) {
            StaticLogger.logException(e);
        }
    }
}
