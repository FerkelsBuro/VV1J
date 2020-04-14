import core.observers.FileObserver;
import domain.models.WatchedDirectory;
import domain.services.DirectoryWatcher;
import infrastructure.FileReader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class Startup {
    public static void main(String[] args) {
        System.out.println("main_start");

        Path path = args.length == 0 ? Paths.get(System.getProperty("user.dir")) : Paths.get(args[0]);

        Logger logger = Logger.getLogger(DirectoryWatcher.class.getName());
        FileReader fileReader = new FileReader();
        DirectoryWatcher directoryWatcher = new DirectoryWatcher(fileReader);
        WatchedDirectory watchedDirectory = new WatchedDirectory(fileReader);
        FileObserver fileObserver = new FileObserver(watchedDirectory, path.toString());

        directoryWatcher.addObserver(fileObserver);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    executeDirectoryWatcher(directoryWatcher);
                } catch (IOException e) {
                    logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                } catch (InterruptedException e) {
                    logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                }
            }

            private void executeDirectoryWatcher(DirectoryWatcher directoryWatcher) throws IOException, InterruptedException {
                directoryWatcher.watch(path);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ExecuteWatchedDirectory(fileObserver);
            }

            private void ExecuteWatchedDirectory(FileObserver fileObserver) {
                fileObserver.run();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ExecuteSync(watchedDirectory, System.out);
            }

            private void ExecuteSync(WatchedDirectory watchedDirectory, OutputStream outputStream) {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        System.out.println("ExecuteSync");
                        watchedDirectory.sync(outputStream);
                    }
                } catch (IOException e) {
                    logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                } catch (InterruptedException e) {
                    logger.severe(new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                }
            }
        }).start();

        System.out.println("main_end");
    }
}
