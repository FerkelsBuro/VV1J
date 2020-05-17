package infrastructure;

import domain.models.WatchedFile;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FileReaderTest {
    private final String fileName = ".gitkeep.txt";
    private final Path path = Paths.get(System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "test" + File.separator + "testFiles" +  File.separator + fileName);

    @Test
    public void readReturnWatchedFile() {
        FileReader fileReader = new FileReader();
        WatchedFile watchedFile = fileReader.readReturnWatchedFile(path.toString(), WatchedFile.Status.CREATED);

        assertEquals(watchedFile.getFileName(), fileName);
        assertEquals(watchedFile.getStatus(), WatchedFile.Status.CREATED);
    }
}