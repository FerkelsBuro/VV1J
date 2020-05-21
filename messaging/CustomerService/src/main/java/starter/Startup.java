package starter;

import com.google.gson.Gson;
import core.Constants;
import infrastructure.MessageSender;
import services.RandomOrderGenerator;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Startup {
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws IOException, TimeoutException {
        MessageSender messageSender = new MessageSender(gson);
        messageSender.send(Constants.Queues.OPEN_ORDERS, RandomOrderGenerator.createRandomOrder());
    }
}
