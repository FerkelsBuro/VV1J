package starter.tasks;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;
import network.server.SyncServer;

import java.io.IOException;

public class ServerTask implements Runnable {
    private WatchedDirectory watchedDirectory;

    public ServerTask(WatchedDirectory watchedDirectory) {
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void run() {
        try {
            SyncServer syncServer = new SyncServer(watchedDirectory);
            while (!Thread.currentThread().isInterrupted()) {
                syncServer.sendData();
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            StaticLogger.logException(e);
        } catch (InterruptedException e) {
            StaticLogger.logException(e);
            Thread.currentThread().interrupt();
        }
    }
}
