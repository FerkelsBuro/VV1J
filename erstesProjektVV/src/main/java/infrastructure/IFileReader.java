package infrastructure;

import domain.models.WatchedFile;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface for FileReaders
 */
public interface IFileReader {
    Collection<String> readPaths(String path) throws IOException;

    Collection<String> readFileNames(String path) throws IOException;

    WatchedFile readReturnWatchedFile(String path, WatchedFile.Status status);
}
