package infrastructure;

import Domain.models.WatchedFile;
import Domain.services.WatchedDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    public Collection<WatchedFile> readReturnWatchedFiles(String path) throws IOException {

        Collection<WatchedFile> files = new LinkedList<>();
        Collection<String> fileNames;

        fileNames = read(path);
        for(String fileName : fileNames) {
            files.add(new WatchedFile(new File(fileName).getName(), path, getFileTime(fileName), WatchedFile.Status.CREATED));
        }

        return files;
    }

    public Collection<String> read(String path) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(path));
        List<String> files = new LinkedList<>();
        paths
                .filter(Files::isRegularFile)
                .forEach(f -> files.add(f.toString()));
        return files;
    }

    public Date getFileTime(String path) {
        return new Date(new File(path).lastModified());
    }
}
