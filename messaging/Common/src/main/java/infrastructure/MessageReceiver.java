package infrastructure;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import domain.models.Order;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class MessageReceiver {
    private static ConnectionFactory factory = new ConnectionFactory();
    private final static Gson gson = new Gson();
    static {
        factory.setHost("localhost");
    }

    public void receive(String queueName, Consumer<Order> onMessageReceive) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = ((Connection) connection).createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Order order = gson.fromJson(message, Order.class);
            onMessageReceive.accept(order);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
