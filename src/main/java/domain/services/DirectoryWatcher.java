package domain.services;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedFile;
import infrastructure.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.function.Consumer;

public class DirectoryWatcher {
    private FileReader fileReader;

    public DirectoryWatcher(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public void watch(Path path, Consumer<FileEvent> strategy) throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();
        registerWatcher(path, watchService);

        readFilesOnStart(path, strategy);

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

    private void registerWatcher(Path path, WatchService watchService) throws IOException {
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    private void readFilesOnStart(Path path, Consumer<FileEvent> strategy) throws IOException {
        Collection<WatchedFile> events = this.fileReader.readReturnWatchedFiles(path.toString());
        for (WatchedFile event : events) {
            strategy.accept(new FileEvent(event, Alphabet.CREATE));
        }
    }
}
