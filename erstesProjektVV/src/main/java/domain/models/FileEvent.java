package domain.models;

import java.util.Objects;

/**
 * Contains the Name and the current event(Alphabet) of that file
 */
public class FileEvent {
    private final String fileName;
    private final Alphabet event;

    public FileEvent(String fileName, Alphabet event) {
        this.fileName = fileName;
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEvent event1 = (FileEvent) o;
        return Objects.equals(fileName, event1.fileName) &&
                event == event1.event;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, event);
    }

    public String getFileName() {
        return fileName;
    }

    public Alphabet getEvent() {
        return event;
    }
}