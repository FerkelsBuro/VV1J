package infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import core.loggers.StaticLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class MessageReceiver {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Gson gson;

    public MessageReceiver(Gson gson) throws FileNotFoundException {
        this(gson, JsonConfigReader.DEFAULT_CONFIG_PATH);
    }

    public MessageReceiver(Gson gson, String configPath) throws FileNotFoundException {
        this.gson = gson;
        JsonConfigReader.readConfigJson(configPath, this.factory);
    }

    public <T> void receive(String queueName, Consumer<T> onMessageReceive, Class<T> clazz) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        StaticLogger.logger.info(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            T order = gson.fromJson(message, (Type) clazz);
            StaticLogger.logger.info(" [x] Received '" + message + "'");
            onMessageReceive.accept(order);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
