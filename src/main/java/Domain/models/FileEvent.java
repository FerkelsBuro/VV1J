package Domain.models;

public class FileEvent {
    private final WatchedFile file;
    private final Alphabet event;

    public FileEvent(WatchedFile file, Alphabet event) {
        this.file = file;
        this.event = event;
    }

    public WatchedFile getFile() {
        return file;
    }

    public Alphabet getEvent() {
        return event;
    }
}
