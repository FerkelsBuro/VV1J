package starter;

import network.server.SyncServer;
import starter.tasks.DirectoryWatcherTask;
import starter.tasks.FileObserverTask;
import starter.tasks.SyncTask;
import core.observers.FileObserver;
import domain.models.WatchedDirectory;
import domain.services.DirectoryWatcher;
import infrastructure.FileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Startup {
    public static void main(String[] args) {
        Path path = Paths.get(args.length == 0 ? System.getProperty("user.dir") : args[0]);

        FileReader fileReader = new FileReader();
        DirectoryWatcher directoryWatcher = new DirectoryWatcher(fileReader);
        WatchedDirectory watchedDirectory = new WatchedDirectory(fileReader);
        FileObserver fileObserver = new FileObserver(watchedDirectory, path.toString());

        directoryWatcher.addObserver(fileObserver);

        new Thread(new DirectoryWatcherTask(directoryWatcher, path)).start();
        new Thread(new FileObserverTask(fileObserver)).start();
        new Thread(new SyncTask(watchedDirectory, System.out)).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SyncServer syncServer = new SyncServer(watchedDirectory);
                    while(true) {
                        syncServer.sendData();
                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
