package domain.services;

import domain.models.FileEvent;
import domain.models.WatchedFile;
import infrastructure.IFileReader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class WatchedDirectory {
    private Map<String, WatchedFile> files = new HashMap<>();
    private IFileReader fileReader;

    public WatchedDirectory(IFileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Map<String, WatchedFile> getFiles() {
        return files;
    }

    public void update(FileEvent event, Path directory) {
        WatchedFile file = files.get(event.getFileName());
        if (file == null) {
            file = fileReader.readReturnWatchedFile(directory.toString() + "\\" + event.getFileName(), WatchedFile.Status.CREATED);
            files.put(file.getFileName(), file);
        } else {
            WatchedFile.Status status = FileStateMachine.getState(file, event.getEvent());
            file.setStatus(status);
            if (status == WatchedFile.Status.MODIFIED) {
                file.setDate(fileReader.getFileTime(file.getDirectory() + file.getFileName()));
            }
        }
    }
}
