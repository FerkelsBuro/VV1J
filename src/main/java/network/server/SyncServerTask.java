package network.server;

import core.loggers.StaticLogger;
import network.client.SyncClient;
import starter.tasks.DirectoryWatcherTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

public class SyncServerTask implements Runnable {
    private final ServerSocket serverSocket;
    private final Collection<Socket> clients;
    private Logger logger = Logger.getLogger(DirectoryWatcherTask.class.getName());

    public SyncServerTask(ServerSocket serverSocket, Collection<Socket> clients) {
        this.serverSocket = serverSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        while(true) {
            try {
                clients.add(serverSocket.accept());
            } catch (IOException e) {
                StaticLogger.logException(e);
            }
        }
    }
}
