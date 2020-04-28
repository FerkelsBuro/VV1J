package infrastructure;

import domain.models.WatchedFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FileReader that can read files of a directory (doesn't read files of sub-directories)
 */
public class FileReader implements IFileReader {
    @Override
    public Collection<String> readPaths(String path) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(path), 1);

        return paths
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Collection<String> readFileNames(String path) throws IOException {
        return readPaths(path).stream()
                .map(p -> new File(p).getName())
                .collect(Collectors.toList());
    }

    @Override
    public Date getFileTime(String path) {
        return new Date(new File(path).lastModified());
    }

    @Override
    public WatchedFile readReturnWatchedFile(String path, WatchedFile.Status status) {
        File file = new File(path);
        return new WatchedFile(file.getName(), file.getParent(), getFileTime(path), status);
    }
}
