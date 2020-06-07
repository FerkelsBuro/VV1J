package infrastructure;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import core.loggers.StaticLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class MessageSender {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Gson gson;

    public MessageSender(Gson gson) throws FileNotFoundException {
        this(gson, JsonConfigReader.DEFAULT_CONFIG_PATH);
    }

    public MessageSender(Gson gson, String configPath) throws FileNotFoundException {
        this.gson = gson;
        JsonConfigReader.readConfigJson(configPath, this.factory);
    }

    public <T> void send(String exchangeName, T message) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //channel.queueDeclare(exchangeName, false, false, false, null);
            channel.exchangeDeclare(exchangeName, "fanout");
            String json = gson.toJson(message);
            channel.basicPublish(exchangeName, "", null, json.getBytes(StandardCharsets.UTF_8));
            StaticLogger.logger.info(" [x] Sent to " + exchangeName + " '" + json + "'");
        }
    }
}
