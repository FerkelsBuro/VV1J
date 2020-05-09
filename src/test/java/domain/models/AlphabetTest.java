package domain.models;

import org.junit.Test;

import java.nio.file.StandardWatchEventKinds;

import static org.junit.Assert.*;

public class AlphabetTest {

    @Test
    public void convertWatchEvents() {
        assertSame(Alphabet.CREATE, Alphabet.convertWatchEvents(StandardWatchEventKinds.ENTRY_CREATE));
        assertSame(Alphabet.MODIFY, Alphabet.convertWatchEvents(StandardWatchEventKinds.ENTRY_MODIFY));
        assertSame(Alphabet.DELETE, Alphabet.convertWatchEvents(StandardWatchEventKinds.ENTRY_DELETE));
    }
}