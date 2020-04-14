package core.observers;

import domain.models.FileEvent;

public interface IFileObserver extends Runnable {
    void onFileEvent(FileEvent fileEvent);
}
