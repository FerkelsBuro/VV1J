package core.observers;

import core.loggers.StaticLogger;
import domain.models.FileEvent;
import domain.models.WatchedDirectory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FileObserver that listens for FileEvents and updates a WatchedDirectory with those FileEvents
 * Gets notified on each FileEvent and logs them
 */
public class FileObserver implements IFileObserver {
    private WatchedDirectory watchedDirectory;
    private BlockingQueue<FileEvent> fileEvents = new LinkedBlockingQueue<>();
    private String path;

    public FileObserver(WatchedDirectory watchedDirectory, String path) {
        this.watchedDirectory = watchedDirectory;
        this.path = path;
    }

    @Override
    public void onFileEvent(FileEvent event) {
        fileEvents.add(event);
    }


    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {
                FileEvent event = fileEvents.take();
                StaticLogger.logger.info(
                        () -> "Event kind: " + event.getEvent()
                                + "\nFile affected: " + event.getFileName()
                                + "\nDirectory: " + path + "\n");

                watchedDirectory.update(event, path);
            }
        } catch (InterruptedException e) {
            StaticLogger.logException(e);
            Thread.currentThread().interrupt();
        }
    }

    public BlockingQueue<FileEvent> getFileEvents() {
        return fileEvents;
    }
}

