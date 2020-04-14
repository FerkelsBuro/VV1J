package core.observers;

import domain.models.FileEvent;
import domain.models.WatchedDirectory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class FileObserver implements IFileObserver, Runnable {
    private Logger logger = Logger.getLogger(FileObserver.class.getName());
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
            while (true) {
                FileEvent event = fileEvents.take();
                logger.info(
                        "Event kind: " + event.getEvent()
                                + "\nFile affected: " + event.getFileName()
                                + "\nDirectory: " + path + "\n");

                watchedDirectory.update(event, path);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

