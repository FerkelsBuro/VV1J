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

    public WatchedFile(String fileName, String directory, Date date, Status status) {
        this.fileName = fileName;
        this.directory = directory;
        this.date = date;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
