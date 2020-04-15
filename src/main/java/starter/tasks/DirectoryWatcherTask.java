package starter.tasks;

import domain.services.DirectoryWatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class DirectoryWatcherTask implements Runnable{
    private DirectoryWatcher directoryWatcher;
    private Path path;
    private Logger logger = Logger.getLogger(DirectoryWatcherTask.class.getName());

    public DirectoryWatcherTask(DirectoryWatcher directoryWatcher, Path path) {
        this.directoryWatcher = directoryWatcher;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            directoryWatcher.watch(path);
        } catch (IOException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }
    }
}