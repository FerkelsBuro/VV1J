package infrastructure;

import Domain.models.WatchedFile;

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

public class FileReader {
    public Collection<WatchedFile> readReturnWatchedFiles(String path) throws IOException {
        Collection<WatchedFile> files = new LinkedList<>();
        Collection<String> fileNames = read(path);

        for (String fileName : fileNames) {
            var file = new File(fileName);
            files.add(new WatchedFile(file.getName(), file.getParent(), getFileTime(fileName), WatchedFile.Status.CREATED));
        }

        return files;
    }

    public Collection<String> read(String path) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(path), 1);

        return paths
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Date getFileTime(String path) {
        return new Date(new File(path).lastModified());
    }
}
