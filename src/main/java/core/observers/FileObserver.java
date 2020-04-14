package core.observers;

import domain.models.FileEvent;
import domain.models.WatchedDirectory;

import java.nio.file.Path;
import java.util.logging.Logger;

public class FileObserver implements IFileObserver {
    private Logger logger = Logger.getLogger(FileObserver.class.getName());
    private WatchedDirectory watchedDirectory;

    public FileObserver(WatchedDirectory watchedDirectory) {
        this.watchedDirectory = watchedDirectory;
    }

    @Override
    public void onFileEvent(FileEvent event, Path path) {
        logger.info(
                "Event kind: " + event.getEvent()
                        + "\nFile affected: " + event.getFileName()
                        + "\nDirectory: " + path.toString() + "\n");

        watchedDirectory.update(event, path);
    }
}
