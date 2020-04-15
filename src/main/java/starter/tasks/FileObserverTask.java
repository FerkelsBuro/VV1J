package starter.tasks;

import core.observers.FileObserver;

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
