package Domain.services;

import Domain.models.Alphabet;
import Domain.models.FileEvent;
import Domain.models.WatchedFile;
import infrastructure.FileReader;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WatchedDirectory {
    private static HashMap<WatchedFile.Status, HashMap<Alphabet, WatchedFile.Status>> stateMachine;

    static {
        stateMachine = new HashMap<WatchedFile.Status, HashMap<Alphabet, WatchedFile.Status>>() {{
            put(WatchedFile.Status.CREATED, new HashMap<Alphabet, WatchedFile.Status>() {{
                put(Alphabet.CREATE, WatchedFile.Status.CREATED);
                put(Alphabet.DELETE, WatchedFile.Status.GONE);
                put(Alphabet.MODIFY, WatchedFile.Status.CREATED);
                put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
            }});
            put(WatchedFile.Status.DELETED, new HashMap<Alphabet, WatchedFile.Status>() {{
                put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
                put(Alphabet.DELETE, WatchedFile.Status.DELETED);
                put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
                put(Alphabet.SYNC, WatchedFile.Status.GONE);
            }});
            put(WatchedFile.Status.GONE, new HashMap<Alphabet, WatchedFile.Status>() {{
                put(Alphabet.CREATE, WatchedFile.Status.CREATED);
                put(Alphabet.DELETE, WatchedFile.Status.GONE);
                put(Alphabet.MODIFY, WatchedFile.Status.GONE);
                put(Alphabet.SYNC, WatchedFile.Status.GONE);
            }});
            put(WatchedFile.Status.INSYNC, new HashMap<Alphabet, WatchedFile.Status>() {{
                put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
                put(Alphabet.DELETE, WatchedFile.Status.DELETED);
                put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
                put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
            }});
            put(WatchedFile.Status.MODIFIED, new HashMap<Alphabet, WatchedFile.Status>() {{
                put(Alphabet.CREATE, WatchedFile.Status.MODIFIED);
                put(Alphabet.DELETE, WatchedFile.Status.DELETED);
                put(Alphabet.MODIFY, WatchedFile.Status.MODIFIED);
                put(Alphabet.SYNC, WatchedFile.Status.INSYNC);
            }});
        }};
    }

    private Map<String, WatchedFile> files = new HashMap<>();

    public WatchedDirectory() {
//        Collection<WatchedFile> watchedFiles = new FileReader().readReturnWatchedFiles(path);
//        for (WatchedFile watchedFile : watchedFiles) {
//            files.put(watchedFile.getFileName(), watchedFile);
//        }
    }

    public void update(FileEvent event, String path) {
        WatchedFile file = files.get(event.getFile().getFileName());
        if(file == null) {
            files.put(event.getFile().getFileName(), new WatchedFile(event.getFile().getFileName(), path, event.getFile().getDate(), WatchedFile.Status.CREATED));
        } else {
            file.setStatus(stateMachine.get(file.getStatus()).get(event.getEvent()));
        }
    }
}
