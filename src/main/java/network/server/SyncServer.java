package network.server;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SyncServer has a list of clients and a WatchedDirectory
 * It can send all clients the output of WatchedDirectory.sync()
 */
public class SyncServer implements Runnable {
    private ServerSocket serverSocket = new ServerSocket(6868);
    private WatchedDirectory watchedDirectory;

    public SyncServer(WatchedDirectory watchedDirectory) throws IOException {
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();
                new Thread(new SyncClientHandler(client, watchedDirectory)).start();
            } catch (IOException e) {
                StaticLogger.logException(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
