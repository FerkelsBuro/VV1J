package infrastructure;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import core.loggers.StaticLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * The MessageReceiver can receive messages from an exchange
 */
public class MessageReceiver {
    private final Channel channel;
    private final Gson gson;

    public MessageReceiver(Gson gson) throws IOException, TimeoutException {
        this(gson, JsonConfigReader.DEFAULT_CONFIG_PATH);
    }

    public MessageReceiver(Gson gson, String configPath) throws IOException, TimeoutException {
        this.gson = gson;
        ConnectionFactory factory = new ConnectionFactory();
        JsonConfigReader.readConfigJson(configPath, factory);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    /**
     * receives messages from a queue of an exchange and calls onMessageReceive if a message is received
     *
     * @param exchangeName     exchangeName
     * @param queueName        queueName
     * @param onMessageReceive Consumer that decides what happens when a message is received
     * @param clazz            class of the message
     * @param <T>
     * @throws IOException
     */
    public <T> void receive(String exchangeName, String queueName, Consumer<T> onMessageReceive, Class<T> clazz) throws IOException {
        channel.exchangeDeclare(exchangeName, "fanout");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, "");

        StaticLogger.logger.info(" [*] Waiting for messages. To exit press CTRL+C\n");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            T order = gson.fromJson(message, (Type) clazz);
            StaticLogger.logger.info(() -> " [x] Received from " + exchangeName + " '" + message + "'");
            onMessageReceive.accept(order);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
