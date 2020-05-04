package starter.tasks;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;

import java.io.OutputStream;

/**
 * Thread that executes the WatchedDirectory.sync() method in a certain time-interval
 */
public class SyncTask implements Runnable {
    private WatchedDirectory watchedDirectory;
    private OutputStream outputStream;
    private final long syncTimeInterval;


    public SyncTask(WatchedDirectory watchedDirectory, OutputStream outputStream, long syncTimeInterval) {
        this.watchedDirectory = watchedDirectory;
        this.outputStream = outputStream;
        this.syncTimeInterval = syncTimeInterval;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(syncTimeInterval);
                watchedDirectory.sync(outputStream);
            }
        } catch (InterruptedException e) {
            StaticLogger.logException(e);
            Thread.currentThread().interrupt();
        }
    }
}
