package starter.tasks;

import core.loggers.StaticLogger;
import domain.services.DirectoryWatcher;

import java.io.IOException;
import java.nio.file.Path;

public class DirectoryWatcherTask implements Runnable {
    private DirectoryWatcher directoryWatcher;
    private Path path;

    public DirectoryWatcherTask(DirectoryWatcher directoryWatcher, Path path) {
        this.directoryWatcher = directoryWatcher;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            directoryWatcher.watch(path);
        } catch (IOException e) {
            StaticLogger.logException(e);
        } catch (InterruptedException e) {
            StaticLogger.logException(e);
            Thread.currentThread().interrupt();
        }
    }
}