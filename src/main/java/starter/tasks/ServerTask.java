package starter.tasks;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;
import network.server.SyncServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class ServerTask implements Runnable {
    private WatchedDirectory watchedDirectory;
    private Logger logger = Logger.getLogger(SyncTask.class.getName());

    public ServerTask(WatchedDirectory watchedDirectory) {
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void run() {
        try {
            SyncServer syncServer = new SyncServer(watchedDirectory);
            while(true) {
                syncServer.sendData();
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            StaticLogger.logException(e);
        }
    }
}
