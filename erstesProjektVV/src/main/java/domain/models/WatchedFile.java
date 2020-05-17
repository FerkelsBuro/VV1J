package domain.models;

import java.util.Date;

/**
 * contains most important attributes of a file and their status
 */
public class WatchedFile {
    private final String fileName;
    private final String directory;
    private Date date;
    private Status status;

    public WatchedFile(String fileName, String directory, Status status) {
        this.fileName = fileName;
        this.directory = directory;
        this.date = new Date(System.currentTimeMillis());
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectory() {
        return directory;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        date.setTime(System.currentTimeMillis());
        this.status = status;
    }

    public enum Status {
        INSYNC,
        CREATED,
        MODIFIED,
        DELETED,
        GONE
    }
}
