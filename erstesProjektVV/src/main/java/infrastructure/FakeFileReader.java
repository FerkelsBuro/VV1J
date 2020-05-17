package infrastructure;

import domain.models.WatchedFile;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

/**
 * FakeFileReader for test-methods
 */
public class FakeFileReader implements IFileReader {
    @Override
    public Collection<String> readPaths(String path) {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> readFileNames(String path) {
        return Collections.emptyList();
    }

    @Override
    public WatchedFile readReturnWatchedFile(String path, WatchedFile.Status status) {
        File file = new File(path);
        return new WatchedFile(file.getName(), file.getParent(), status);
    }
}
