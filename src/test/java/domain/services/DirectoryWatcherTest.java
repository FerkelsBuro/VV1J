package domain.services;

import core.observers.IFileObserver;
import domain.models.Alphabet;
import domain.models.FileEvent;
import infrastructure.FileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DirectoryWatcherTest {
    IFileObserver fileObserver;
    DirectoryWatcher directoryWatcher;

    @Before
    public void Setup() {
        fileObserver = mock(IFileObserver.class);
        directoryWatcher = new DirectoryWatcher(new FileReader());
    }

    @Test
    public void watch() throws IOException, InterruptedException {
        Path path = Paths.get(System.getProperty("user.dir") + File.separator + "test");
        Path filePath = Paths.get(path.toString() + File.separator + "testfile.txt");

        Files.createDirectory(path);
        Files.createFile(filePath);

        directoryWatcher.addObserver(fileObserver);

        Thread thread = new Thread(() -> {
            try {
                directoryWatcher.watch(path);
            } catch (IOException | InterruptedException ignored) {
            }
        });

        thread.start();
        Thread.sleep(100);
        thread.interrupt();

        Files.delete(filePath);
        Files.delete(path);

        verify(fileObserver).onFileEvent(new FileEvent("testfile.txt", Alphabet.CREATE));
    }
}