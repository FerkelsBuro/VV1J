package domain.services;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedDirectory;
import domain.models.WatchedFile;
import infrastructure.FakeFileReader;
import org.junit.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WatchedDirectoryTest {

    @Test
    public void update_testCreateNewFile() {
        WatchedDirectory watchedDirectory = new WatchedDirectory(new FakeFileReader());

        FileEvent event = new FileEvent("file", Alphabet.CREATE);
        Path path = Path.of("");
        watchedDirectory.update(event, path);

        assertEquals(WatchedFile.Status.CREATED, watchedDirectory.getFiles().get(event.getFileName()).getStatus());
    }

    @Test
    public void update_deleteCreatedFile() {
        WatchedFile watchedFile = new WatchedFile("file", "", null, WatchedFile.Status.CREATED);
        Map<String, WatchedFile> files = new HashMap<>() {{
            put(watchedFile.getFileName(), watchedFile);
        }};
        WatchedDirectory watchedDirectory = new WatchedDirectory(files, new FakeFileReader());

        FileEvent event = new FileEvent(watchedFile.getFileName(), Alphabet.DELETE);
        watchedDirectory.update(event, Path.of(watchedFile.getDirectory()));

        assertEquals(WatchedFile.Status.GONE, watchedDirectory.getFiles().get(event.getFileName()).getStatus());
    }
}