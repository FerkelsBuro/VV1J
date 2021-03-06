package Domain.models;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;

public enum Alphabet {
    CREATE,
    DELETE,
    SYNC,
    MODIFY;

    public static Map<WatchEvent.Kind<?>, Alphabet> watchEventToAlphabet = new HashMap<>();

    static {
        watchEventToAlphabet.put(StandardWatchEventKinds.ENTRY_CREATE, CREATE);
        watchEventToAlphabet.put(StandardWatchEventKinds.ENTRY_MODIFY, MODIFY);
        watchEventToAlphabet.put(StandardWatchEventKinds.ENTRY_DELETE, DELETE);
    }

    public static Alphabet convertWatchEvents(WatchEvent.Kind<?> eventKinds) {
        return watchEventToAlphabet.get(eventKinds);
    }
}
