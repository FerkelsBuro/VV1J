package domain.services;

import domain.models.Alphabet;
import domain.models.FileEvent;
import domain.models.WatchedFile;
import infrastructure.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
            key.pollEvents().stream()
                    .filter(e -> !new File(e.context().toString()).isDirectory())
                    .map(e -> new FileEvent(e.context().toString(), Alphabet.convertWatchEvents(e.kind())))
                    .forEach(strategy);

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
        Collection<String> fileNames = this.fileReader.readFileNames(path.toString());
        for (String fileName : fileNames) {
            strategy.accept(new FileEvent(fileName, Alphabet.CREATE));
        }
    }
}
