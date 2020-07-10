package th.vv3;

import com.google.gson.Gson;
import core.Constants;
import infrastructure.MessageSender;
import th.vv3.models.Customer;
import th.vv3.models.Order;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestService {
    private static Gson gson = new Gson();
    private MessageSender messageSender = new MessageSender(gson);

    public TestService() throws IOException {
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        MessageSender messageSender = new MessageSender(gson);

        Customer customer = new Customer("Unknown", "bfasfdsafas", "cfdsafdsaf", "dfdsafdsafsafsa");
        Order order = new Order(700, customer);
        order.setApprovedBy("blabla");

        messageSender.send(Constants.Exchanges.APPROVED_ORDERS, order);
        messageSender.send(Constants.Exchanges.APPROVED_ORDERS, order);
        messageSender.send(Constants.Exchanges.APPROVED_ORDERS, order);
    }

}
