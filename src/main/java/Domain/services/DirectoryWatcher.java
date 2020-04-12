package Domain.services;

import Domain.models.Alphabet;
import Domain.models.FileEvent;
import Domain.models.WatchedFile;
import infrastructure.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DirectoryWatcher {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(DirectoryWatcher.class.getName());
        Path path;

        if (args.length == 0) {
            path = Paths.get(System.getProperty("user.dir"));
        } else {
            path = Paths.get(args[0]);
        }

        DirectoryWatcher directoryWatcher = new DirectoryWatcher();
        WatchedDirectory watchedDirectory = new WatchedDirectory();

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

    public void watch(Path path, Consumer<FileEvent> strategy) throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        Collection<WatchedFile> events = new FileReader().readReturnWatchedFiles(path.toString());
        for (WatchedFile event : events) {
            strategy.accept(new FileEvent(event, Alphabet.CREATE));
        }

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                File currentFile = new File(event.context().toString());
                if (!currentFile.isDirectory()) {
                    strategy.accept(new FileEvent(new WatchedFile(event.context().toString(), new File(currentFile.getCanonicalPath()).getParent(), null, null), Alphabet.convertWatchEvents(event.kind())));
                }
            }
            key.reset();
        }
    }
}
