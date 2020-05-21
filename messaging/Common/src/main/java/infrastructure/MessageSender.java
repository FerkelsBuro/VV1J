package infrastructure;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MessageSender {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Gson gson;

    public MessageSender(Gson gson) {
        this.gson = gson;
        factory.setHost("localhost");
    }

    public <T> void send(String queueName, T message) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            String json = gson.toJson(message);
            channel.basicPublish("", queueName, null, json.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + json + "'");
        }
    }
}
