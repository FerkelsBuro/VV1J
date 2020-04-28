package network.server;

import core.loggers.StaticLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

/**
 * Thread that accepts clients for the server
 */
public class SyncServerTask implements Runnable {
    private final ServerSocket serverSocket;
    private final Collection<Socket> clients;

    public SyncServerTask(ServerSocket serverSocket, Collection<Socket> clients) {
        this.serverSocket = serverSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                clients.add(serverSocket.accept());
            } catch (IOException e) {
                StaticLogger.logException(e);
            }
        }
    }
}
