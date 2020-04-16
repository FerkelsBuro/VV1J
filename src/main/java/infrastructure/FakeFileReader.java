package infrastructure;

import domain.models.WatchedFile;

import java.io.File;
import java.util.Collection;
import java.util.Date;

public class FakeFileReader implements IFileReader {
    @Override
    public Collection<String> readPaths(String path) {
        return null;
    }

    @Override
    public Collection<String> readFileNames(String path) {
        return null;
    }

    @Override
    public Date getFileTime(String path) {
        return null;
    }

    @Override
    public WatchedFile readReturnWatchedFile(String path, WatchedFile.Status status) {
        File file = new File(path);
        return new WatchedFile(file.getName(), file.getParent(), getFileTime(path), status);
    }
}
