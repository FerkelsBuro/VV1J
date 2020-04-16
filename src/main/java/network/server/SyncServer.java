package network.server;

import domain.models.WatchedDirectory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class SyncServer {
    private ServerSocket serverSocket = new ServerSocket(6868);
    private Vector<Socket> clients = new Vector<>();
    private WatchedDirectory watchedDirectory;

    public SyncServer(WatchedDirectory watchedDirectory) throws IOException {
        this.watchedDirectory = watchedDirectory;
        acceptClients();
    }

    private void acceptClients() {
        new Thread(new SyncServerTask(serverSocket, clients)).start();
    }

    public void sendData() throws IOException {
        for (Socket socket : clients) {
            OutputStream output = socket.getOutputStream();
            watchedDirectory.sync(output);
        }
    }
}
