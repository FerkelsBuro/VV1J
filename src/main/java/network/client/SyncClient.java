package network.client;

import core.loggers.StaticLogger;
import starter.tasks.DirectoryWatcherTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * SyncClient that doesn't do a whole lot except for logging
 */
public class SyncClient {
    private static Logger logger = Logger.getLogger(DirectoryWatcherTask.class.getName());
    private BufferedReader in;

    public SyncClient(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void main(String[] args) {
        try {
            SyncClient syncClient = new SyncClient(new Socket("127.0.0.1", 6868));
            while (!Thread.currentThread().isInterrupted()) {
                logger.info(syncClient.getNextLine());
            }
        } catch (IOException e) {
            StaticLogger.logException(e);
        }


    }

    public String getNextLine() throws IOException {
        return in.readLine();
    }
}
