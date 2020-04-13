package domain.services;

import domain.models.Alphabet;
import domain.models.WatchedFile;

import java.util.HashMap;

public class FileStateMachine {
    private static final HashMap<WatchedFile.Status, HashMap<Alphabet, WatchedFile.Status>> stateMachine = new HashMap<>() {{
        put(WatchedFile.Status.CREATED, new HashMap<>() {{
            put(Alphabet.CREATE, WatchedFile.Status.CREATED);
            put(Alphabet.DELETE, WatchedFile.Status.GONE);
            put(Alphabet.MODIFY, WatchedFile.Status.CREATED);
            put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
        }});
        put(WatchedFile.Status.DELETED, new HashMap<>() {{
            put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
            put(Alphabet.DELETE, WatchedFile.Status.DELETED);
            put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
            put(Alphabet.SYNC, WatchedFile.Status.GONE);
        }});
        put(WatchedFile.Status.GONE, new HashMap<>() {{
            put(Alphabet.CREATE, WatchedFile.Status.CREATED);
            put(Alphabet.DELETE, WatchedFile.Status.GONE);
            put(Alphabet.MODIFY, WatchedFile.Status.GONE);
            put(Alphabet.SYNC, WatchedFile.Status.GONE);
        }});
        put(WatchedFile.Status.INSYNC, new HashMap<>() {{
            put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
            put(Alphabet.DELETE, WatchedFile.Status.DELETED);
            put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
            put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
        }});
        put(WatchedFile.Status.MODIFIED, new HashMap<>() {{
            put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
            put(Alphabet.DELETE, WatchedFile.Status.DELETED);
            put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
            put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
        }});
    }};

    public static WatchedFile.Status getState(WatchedFile file, Alphabet input) {
        return stateMachine.get(file.getStatus()).get(input);
    }
}
