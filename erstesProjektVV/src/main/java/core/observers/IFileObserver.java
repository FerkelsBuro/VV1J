package core.observers;

import domain.models.FileEvent;

/**
 * Interface for FileObservers that do something on FileEvents
 * FileObservers implement the Runnable interface
 */
public interface IFileObserver extends Runnable {
    void onFileEvent(FileEvent fileEvent);
}
