package starter;

import com.google.gson.Gson;
import core.Constants;
import infrastructure.MessageSender;
import services.RandomOrderGenerator;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Startup {
    private final static Gson gson = new Gson();
    private final static int DEFAULT_INTERVAL_IN_MILLISECONDS = 1000;

    public static void main(String[] argv) throws IOException, TimeoutException, InterruptedException {
        int interval = argv.length > 0 ? Integer.parseInt(argv[0]) : DEFAULT_INTERVAL_IN_MILLISECONDS;

        MessageSender messageSender = new MessageSender(gson);

        while(!Thread.interrupted()) {
            messageSender.send(Constants.Exchanges.OPEN_ORDERS, RandomOrderGenerator.createRandomOrder());
            Thread.sleep(interval);
        }
    }
}
