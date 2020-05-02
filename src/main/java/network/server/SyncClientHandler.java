package network.server;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SyncClientHandler implements Runnable {
    private OutputStream output;
    private WatchedDirectory watchedDirectory;

    public SyncClientHandler(Socket client, WatchedDirectory watchedDirectory) throws IOException {
        output = client.getOutputStream();
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            watchedDirectory.sync(output);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StaticLogger.logException(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}