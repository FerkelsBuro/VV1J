package network.server;

import domain.models.WatchedDirectory;
import domain.models.WatchedFile;
import infrastructure.FakeFileReader;
import network.client.SyncClient;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SyncServerTest {

    @Test
    public void sendData_integrationTest() throws IOException {
        WatchedFile watchedFile = new WatchedFile("file", "d", new Date(), WatchedFile.Status.CREATED);
        Map<String, WatchedFile> files = new HashMap<>();
        files.put(watchedFile.getFileName(), watchedFile);

        WatchedDirectory watchedDirectory = new WatchedDirectory(files, new FakeFileReader());

        var server = new SyncServer(watchedDirectory);
        var client = new SyncClient(new Socket("127.0.0.1", 6868));

        server.sendData();
        String result = client.getNextLine();

        assertNotEquals("", result);
        assertNotNull(result);
        assertTrue(result.length() > 5);
    }
}