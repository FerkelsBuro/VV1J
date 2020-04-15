package console.tasks;

import domain.models.WatchedDirectory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class SyncTask implements Runnable  {
    private WatchedDirectory watchedDirectory;
    private OutputStream outputStream;
    private Logger logger = Logger.getLogger(SyncTask.class.getName());


    public SyncTask(WatchedDirectory watchedDirectory, OutputStream outputStream) {

        this.watchedDirectory = watchedDirectory;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println("ExecuteSync");
                watchedDirectory.sync(outputStream);
            }
        } catch (IOException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }
    }
}
