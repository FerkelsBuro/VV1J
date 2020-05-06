package starter.tasks;

import core.observers.FileObserver;

/**
 * Thread that executes the FileObserver (i.e. logs the file-changes & updates the WatchedDirectory)
 */
public class FileObserverTask implements Runnable {
    private FileObserver fileObserver;

    public FileObserverTask(FileObserver fileObserver) {
        this.fileObserver = fileObserver;
    }

    @Override
    public void run() {
        fileObserver.run();
    }
}
