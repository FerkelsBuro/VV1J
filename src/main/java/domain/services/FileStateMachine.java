package domain.services;

import domain.models.Alphabet;
import domain.models.WatchedFile;
import domain.models.WatchedFile.Status;

import java.util.HashMap;

public class FileStateMachine {
    private static final HashMap<Status, HashMap<Alphabet, Status>> stateMachine = new HashMap<>();


    static {
        addStatusReaction(Status.CREATED, Status.CREATED, Status.GONE, Status.CREATED, Status.INSYNC);
        addStatusReaction(Status.DELETED, Status.MODIFIED, Status.DELETED, Status.MODIFIED, Status.GONE);
        addStatusReaction(Status.GONE, Status.CREATED, Status.GONE, Status.GONE, Status.GONE);
        addStatusReaction(Status.INSYNC, Status.MODIFIED, Status.DELETED, Status.MODIFIED, Status.INSYNC);
        addStatusReaction(Status.MODIFIED, Status.MODIFIED, Status.DELETED, Status.MODIFIED, Status.INSYNC);

    }

    private FileStateMachine() {
    }

    private static void addStatusReaction(Status currentStatus, Status afterCreate,
                                          Status afterDelete, Status afterModify,
                                          Status afterSync) {
        HashMap<Alphabet, Status> temp = new HashMap<>();
        temp.put(Alphabet.CREATE, afterCreate);
        temp.put(Alphabet.DELETE, afterDelete);
        temp.put(Alphabet.MODIFY, afterModify);
        temp.put(Alphabet.SYNC, afterSync);
        stateMachine.put(currentStatus, temp);
    }

    public static Status getState(Status status, Alphabet input) {
        return stateMachine.get(status).get(input);
    }

    public static Status getState(WatchedFile file, Alphabet input) {
        return stateMachine.get(file.getStatus()).get(input);
    }
}
