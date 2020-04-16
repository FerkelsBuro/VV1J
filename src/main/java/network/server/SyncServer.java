package network.server;

import domain.models.WatchedDirectory;
import network.client.SyncClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SyncServer {
    private ServerSocket serverSocket = new ServerSocket(6868);
    private Vector<Socket> clients = new Vector<>();
    private WatchedDirectory watchedDirectory;
    //private Collection<SyncClient> c = Collections.synchronizedCollection(new LinkedList<>());

    public SyncServer(WatchedDirectory watchedDirectory) throws IOException {
        this.watchedDirectory = watchedDirectory;
        acceptClients();
    }

    private void acceptClients() {
        new Thread(new SyncServerTask(serverSocket, clients)).start();
    }

    public void sendData() throws IOException {
        System.out.println("\n" + "connected clients: " + clients);
        for(Socket socket : clients) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            watchedDirectory.sync(output);
            writer.println("");
        }
    }
}
