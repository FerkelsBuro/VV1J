package core.observers;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedDirectory;
import infrastructure.FakeFileReader;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class FileObserverTest {

    @Test
    public void onFileEvent() {
        FileObserver fileObserver = new FileObserver(new WatchedDirectory(new FakeFileReader()), "");
        Collection<FileEvent> fileEvents = fileObserver.getFileEvents();

        fileObserver.onFileEvent(new FileEvent("file", Alphabet.CREATE));

        assertFalse(fileEvents.isEmpty());
    }
}