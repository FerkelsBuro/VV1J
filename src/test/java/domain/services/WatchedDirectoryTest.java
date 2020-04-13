package domain.services;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedDirectory;
import domain.models.WatchedFile;
import infrastructure.FakeFileReader;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class WatchedDirectoryTest {

    private static WatchedFile createFileWithStatus(WatchedDirectory watchedDirectory, WatchedFile.Status status, String fileName) {
        FileEvent event = new FileEvent(fileName, Alphabet.CREATE);
        Path path = Path.of("");
        watchedDirectory.update(event, path);

        if (status == WatchedFile.Status.CREATED) {
            return watchedDirectory.getFiles().get(event.getFileName());
        } else if (status == WatchedFile.Status.INSYNC) {
            watchedDirectory.update(new FileEvent(fileName, Alphabet.SYNC), path);
            return watchedDirectory.getFiles().get(event.getFileName());
        } else if (status == WatchedFile.Status.MODIFIED) {
            watchedDirectory.update(new FileEvent(fileName, Alphabet.SYNC), path);
            watchedDirectory.update(new FileEvent(fileName, Alphabet.MODIFY), path);
            return watchedDirectory.getFiles().get(event.getFileName());
        } else if (status == WatchedFile.Status.DELETED) {
            watchedDirectory.update(new FileEvent(fileName, Alphabet.SYNC), path);
            watchedDirectory.update(new FileEvent(fileName, Alphabet.MODIFY), path);
            watchedDirectory.update(new FileEvent(fileName, Alphabet.DELETE), path);
            return watchedDirectory.getFiles().get(event.getFileName());
        } else {
            watchedDirectory.update(new FileEvent(fileName, Alphabet.DELETE), path);
            return watchedDirectory.getFiles().get(event.getFileName());
        }
    }

    @Test
    public void update_testStateMachine() {
        WatchedDirectory watchedDirectory = new WatchedDirectory(new FakeFileReader());

        FileEvent event = new FileEvent("file", Alphabet.CREATE);
        Path path = Path.of("");
        watchedDirectory.update(event, path);

        assertEquals(WatchedFile.Status.CREATED, watchedDirectory.getFiles().get(event.getFileName()).getStatus());
    }
}