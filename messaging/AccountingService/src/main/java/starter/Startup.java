package starter;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import core.OrderApprovalStrategy;
import domain.models.Order;
import infrastructure.MessageReceiver;

import java.nio.charset.StandardCharsets;

public class Startup {

    private final static String QUEUE_NAME = "OpenOrders";
    private static final OrderApprovalStrategy strategy = new OrderApprovalStrategy();
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws Exception {
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        messageReceiver.receive(QUEUE_NAME, (Order order) -> {
            if (strategy.needsApproval(order)) {

            }
        }, Order.class);
    }
}
