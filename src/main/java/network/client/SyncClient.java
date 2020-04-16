package network.client;

import starter.tasks.DirectoryWatcherTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class SyncClient {
    private static Logger logger = Logger.getLogger(DirectoryWatcherTask.class.getName());;
    private Socket clientSocket;
    private BufferedReader in;

    public SyncClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void main(String[] args) {
        System.out.println("client started\n");
        try {
            SyncClient syncClient = new SyncClient(new Socket("127.0.0.1", 6868));
            while(true) {
                System.out.println("output: ");
                System.out.println(syncClient.in.readLine());
            }
        } catch (IOException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }


    }
}
