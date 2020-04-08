package Domain.models;

import java.util.Date;

public class WatchedFile {
    private String fileName;
    private String directory;
    private Date date;

    public String getFileName() {
        return fileName;
    }

    public String getDirectory() {
        return directory;
    }

    public Date getDate() {
        return date;
    }

    public enum Status {
        INSYNC,
        CREATED,
        MODIFIED,
        DELETED,
        GONE
    }
}
