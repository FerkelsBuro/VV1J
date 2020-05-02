package starter.tasks;

import core.loggers.StaticLogger;
import domain.models.WatchedDirectory;

import java.io.OutputStream;

public class SyncTask implements Runnable {
    private WatchedDirectory watchedDirectory;
    private OutputStream outputStream;


    public SyncTask(WatchedDirectory watchedDirectory, OutputStream outputStream) {

        this.watchedDirectory = watchedDirectory;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                watchedDirectory.sync(outputStream);
            }
        } catch (InterruptedException e) {
            StaticLogger.logException(e);
        }
    }
}
