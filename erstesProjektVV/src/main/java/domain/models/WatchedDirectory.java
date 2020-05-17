package domain.models;

import com.google.gson.Gson;
import domain.services.FileStateMachine;
import infrastructure.IFileReader;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Data-structure that saves files in a directory as a Map of WatchedFiles
 */
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

    /**
     * Updates the stored Files by considering the FileEvent.
     * A new WatchedFile is created when the File of the FileEvent is not already stored, otherwise just updated
     * @param event FileEvent
     * @param directory directory of the File
     */
    public void update(FileEvent event, String directory) {
        WatchedFile file = files.get(event.getFileName());
        if (file == null) {
            file = fileReader.readReturnWatchedFile(directory + File.separator + event.getFileName(), WatchedFile.Status.CREATED);
            files.put(file.getFileName(), file);
        } else {
            WatchedFile.Status status = FileStateMachine.getState(file, event.getEvent());
            file.setStatus(status);
        }
    }

    /**
     * Syncs every stored File (by using the FileStateMachine).
     * outputs all the files to an outputStream as a Json
     * @param outputStream outputStream for the Json
     */
    public void sync(OutputStream outputStream) {
        for (WatchedFile file : files.values()) {
            file.setStatus(FileStateMachine.getState(file, Alphabet.SYNC));
        }

        Gson gson = new Gson();
        String output = gson.toJson(files);
        PrintWriter writer = new PrintWriter(outputStream, true);
        writer.println(output);
    }
}
