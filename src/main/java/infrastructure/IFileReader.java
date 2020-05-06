package infrastructure;

import domain.models.WatchedFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Interface for FileReaders
 */
public interface IFileReader {
    Collection<String> readPaths(String path) throws IOException;

    Collection<String> readFileNames(String path) throws IOException;

    Date getFileTime(String path);

    WatchedFile readReturnWatchedFile(String path, WatchedFile.Status status);
}
