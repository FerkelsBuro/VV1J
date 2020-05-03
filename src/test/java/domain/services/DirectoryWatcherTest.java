package domain.services;

import core.observers.IFileObserver;
import domain.models.Alphabet;
import domain.models.FileEvent;
import infrastructure.FileReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DirectoryWatcherTest {
    private IFileObserver fileObserver;
    private DirectoryWatcher directoryWatcher;
    private final Path directory = Paths.get(System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "test" + File.separator + "testFiles");


    @Before
    public void Setup() {
        fileObserver = mock(IFileObserver.class);
        directoryWatcher = new DirectoryWatcher(new FileReader());
    }

    @Test
    public void watch() throws IOException, InterruptedException {
        String fileName = "testfile.txt";
        Path filePath = Paths.get(directory + File.separator + fileName);

        Files.createFile(filePath);

        directoryWatcher.addObserver(fileObserver);

        Thread thread = new Thread(() -> {
            try {
                directoryWatcher.watch(directory);
            } catch (IOException | InterruptedException ignored) {
            }
        });

        thread.start();
        Thread.sleep(100);
        thread.interrupt();

        verify(fileObserver).onFileEvent(new FileEvent(fileName, Alphabet.CREATE));
    }

    @After
    public void TearDown() {
        for(File file: Objects.requireNonNull(new File(directory.toString()).listFiles()))
            if (!file.isDirectory())
                file.delete();
    }
}