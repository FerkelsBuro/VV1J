package Domain.services;

import Domain.models.Alphabet;
import Domain.models.FileEvent;
import Domain.models.WatchedFile;
import infrastructure.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.function.Consumer;

public class DirectoryWatcher {
    public void watch(Path path, Consumer<FileEvent> strategy) throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        Collection<WatchedFile> events = new FileReader().readReturnWatchedFiles(path.toString());
        for (WatchedFile event : events) {
            strategy.accept(new FileEvent(event, Alphabet.CREATE));
        }

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                File currentFile = new File(event.context().toString());
                if (!currentFile.isDirectory()) {
                    strategy.accept(new FileEvent(new WatchedFile(event.context().toString(), new File(currentFile.getCanonicalPath()).getParent(), null, null), Alphabet.convertWatchEvents(event.kind())));
                }
            }
            key.reset();
        }
    }
}
