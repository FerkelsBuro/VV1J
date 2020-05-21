package starter;

import com.google.gson.Gson;
import infrastructure.MessageSender;
import services.RandomOrderGenerator;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Startup {
    private final static String QUEUE_NAME = "OpenOrders";
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws IOException, TimeoutException {
        MessageSender messageSender = new MessageSender(gson);
        messageSender.send(QUEUE_NAME, RandomOrderGenerator.createRandomOrder());
    }
}
