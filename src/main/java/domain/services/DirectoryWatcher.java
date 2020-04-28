package domain.services;

import core.observers.IFileObserver;
import domain.models.Alphabet;
import domain.models.FileEvent;
import infrastructure.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Watches the files of a directory and notifies IFileObservers for any changes to files
 */
public class DirectoryWatcher {
    private FileReader fileReader;
    private List<IFileObserver> observers;


    public DirectoryWatcher(FileReader fileReader) {
        this.fileReader = fileReader;
        observers = new ArrayList<>();
    }

    public void addObserver(IFileObserver observer) {
        observers.add(observer);
    }

    public void watch(Path path) throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();
        registerWatcher(path, watchService);

        readFilesOnStart(path);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            key.pollEvents().stream()
                    .filter(e -> !new File(e.context().toString()).isDirectory())
                    .map(e -> new FileEvent(e.context().toString(), Alphabet.convertWatchEvents(e.kind())))
                    .forEach(this::executeObservers);

            key.reset();
        }
    }

    private void executeObservers(FileEvent event) {
        for (IFileObserver observer : observers) {
            observer.onFileEvent(event);
        }
    }

    private void registerWatcher(Path path, WatchService watchService) throws IOException {
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    private void readFilesOnStart(Path path) throws IOException {
        Collection<String> fileNames = this.fileReader.readFileNames(path.toString());
        for (String fileName : fileNames) {
            executeObservers(new FileEvent(fileName, Alphabet.CREATE));
        }
    }
}
