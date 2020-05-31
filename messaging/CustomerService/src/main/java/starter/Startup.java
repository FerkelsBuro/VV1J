package starter;

import com.google.gson.Gson;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import services.CustomerService;
import services.RandomOrderGenerator;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Startup {
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws IOException, TimeoutException {
        CustomerService customerService = new CustomerService(new MessageReceiver(gson), new MessageSender(gson));
        customerService.send(RandomOrderGenerator.createRandomOrder());
    }
}
