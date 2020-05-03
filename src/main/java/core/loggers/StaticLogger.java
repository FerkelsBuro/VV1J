package core.loggers;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

/**
 * StaticLogger that allows to log exceptions
 */
public class StaticLogger {
    public static final Logger logger = Logger.getLogger(StaticLogger.class.getName());

    private StaticLogger() {
    }

    public static void logException(Exception e) {
        logger.severe(() -> new Date().toString() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
    }
}
