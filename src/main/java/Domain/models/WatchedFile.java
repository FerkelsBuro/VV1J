package Domain.models;

import java.util.Date;

public class WatchedFile {
    private String fileName;
    private String directory;
    private Date date;

    public enum Status {
        INSYNC,
        CREATED,
        MODIFIED,
        DELETED,
        GONE
    }
}
