package core.observers;

import domain.models.FileEvent;

import java.nio.file.Path;

public interface IFileObserver {
    void onFileEvent(FileEvent fileEvent, Path path);
}
