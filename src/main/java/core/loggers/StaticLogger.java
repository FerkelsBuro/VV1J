package core.loggers;

import starter.tasks.DirectoryWatcherTask;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class StaticLogger {
    private static Logger logger = Logger.getLogger(DirectoryWatcherTask.class.getName());

    private StaticLogger() {
    }

    public static void logException(Exception e) {
        logger.severe(() -> new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
    }
}
