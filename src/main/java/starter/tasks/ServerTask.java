package starter.tasks;

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
            while(true) {
                syncServer.sendData();
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
