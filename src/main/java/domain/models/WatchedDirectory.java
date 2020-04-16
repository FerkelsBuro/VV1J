package domain.models;

import com.google.gson.Gson;
import domain.services.FileStateMachine;
import infrastructure.IFileReader;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class WatchedDirectory {
    private Map<String, WatchedFile> files = new HashMap<>();
    private IFileReader fileReader;

    public WatchedDirectory(Map<String, WatchedFile> files, IFileReader fileReader) {
        this.files = files;
        this.fileReader = fileReader;
    }

    public WatchedDirectory(IFileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Map<String, WatchedFile> getFiles() {
        return files;
    }

    public void update(FileEvent event, String directory) {
        WatchedFile file = files.get(event.getFileName());
        if (file == null) {
            file = fileReader.readReturnWatchedFile(directory + File.separator + event.getFileName(), WatchedFile.Status.CREATED);
            files.put(file.getFileName(), file);
        } else {
            WatchedFile.Status status = FileStateMachine.getState(file, event.getEvent());
            file.setStatus(status);
            if (status == WatchedFile.Status.MODIFIED) {
                file.setDate(fileReader.getFileTime(file.getDirectory() + file.getFileName()));
            }
        }
    }

    public void sync(OutputStream outputStream) {
        Gson gson = new Gson();
        String output = gson.toJson(files);
        PrintWriter writer = new PrintWriter(outputStream, true);
        writer.println(output);

        for (WatchedFile file : files.values()) {
            file.setStatus(FileStateMachine.getState(file, Alphabet.SYNC));
        }
    }
}
