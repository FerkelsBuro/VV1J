package infrastructure;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class MessageReceiver {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Gson gson;

    public MessageReceiver(Gson gson) {
        this.gson = gson;
        factory.setHost("localhost");
    }

    public <T> void receive(String queueName, Consumer<T> onMessageReceive, Class<T> clazz) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = ((Connection) connection).createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            T order = gson.fromJson(message, (Type) clazz);
            onMessageReceive.accept(order);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
