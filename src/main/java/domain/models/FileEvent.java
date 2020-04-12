package domain.models;

public class FileEvent {
    private final String fileName;
    private final Alphabet event;

    public FileEvent(String fileName, Alphabet event) {
        this.fileName = fileName;
        this.event = event;
    }

    public String getFileName() {
        return fileName;
    }

    public Alphabet getEvent() {
        return event;
    }
}
