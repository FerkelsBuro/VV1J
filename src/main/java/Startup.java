import core.observers.FileObserver;
import domain.models.WatchedDirectory;
import domain.services.DirectoryWatcher;
import infrastructure.FileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class Startup {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(DirectoryWatcher.class.getName());
        FileReader fileReader = new FileReader();
        DirectoryWatcher directoryWatcher = new DirectoryWatcher(fileReader);
        WatchedDirectory watchedDirectory = new WatchedDirectory(fileReader);
        FileObserver fileObserver = new FileObserver(watchedDirectory);

        Path path = args.length == 0 ? Paths.get(System.getProperty("user.dir")) : Paths.get(args[0]);
        directoryWatcher.addObserver(fileObserver);

        try {
            directoryWatcher.watch(path);
        } catch (IOException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }
    }
}
