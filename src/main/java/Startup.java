import Domain.models.FileEvent;
import Domain.services.DirectoryWatcher;
import Domain.services.WatchedDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Startup {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(DirectoryWatcher.class.getName());
        DirectoryWatcher directoryWatcher = new DirectoryWatcher();
        WatchedDirectory watchedDirectory = new WatchedDirectory();

        Path path = args.length == 0 ? Paths.get(System.getProperty("user.dir")) : Paths.get(args[0]);

        Consumer<FileEvent> strategy = (event) -> {
            logger.info(
                    "Event kind: " + event.getEvent()
                            + "\nFile affected: " + event.getFile().getFileName()
                            + "\nDirectory: " + event.getFile().getDirectory() + "\n");

            watchedDirectory.update(event);
        };
        try {
            directoryWatcher.watch(path, strategy);
        } catch (IOException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        }
    }
}
