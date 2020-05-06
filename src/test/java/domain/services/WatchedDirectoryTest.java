package domain.services;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedDirectory;
import domain.models.WatchedFile;
import infrastructure.FakeFileReader;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WatchedDirectoryTest {

    @Test
    public void update_testCreateNewFile() {
        WatchedDirectory watchedDirectory = new WatchedDirectory(new FakeFileReader());

        FileEvent event = new FileEvent("file", Alphabet.CREATE);
        Path path = Path.of("");
        watchedDirectory.update(event, path.toString());

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
        watchedDirectory.update(event, watchedFile.getDirectory());

        assertEquals(WatchedFile.Status.GONE, watchedDirectory.getFiles().get(event.getFileName()).getStatus());
    }

    @Test
    public void sync_filesGetStatusSync() throws IOException {
        var statuses = new ArrayList<>(Arrays.asList(
                WatchedFile.Status.CREATED,
                WatchedFile.Status.DELETED,
                WatchedFile.Status.MODIFIED,
                WatchedFile.Status.INSYNC,
                WatchedFile.Status.GONE
        ));

        var fakeOutputStream = new OutputStream() {
            @Override
            public void write(int b) {
            }
        };

        for (var status : statuses) {
            var file = new WatchedFile("file", "", null, status);
            WatchedDirectory watchedDirectory = new WatchedDirectory(new HashMap<>() {{
                put(file.getFileName(), file);
            }}, new FakeFileReader());

            watchedDirectory.sync(fakeOutputStream);

            assertEquals(file.getStatus(), FileStateMachine.getState(status, Alphabet.SYNC));
        }
    }
}