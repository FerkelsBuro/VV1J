package starter;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import core.Constants;
import core.OrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Startup {
    private static final OrderApprovalStrategy strategy = new OrderApprovalStrategy();
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws Exception {
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        MessageSender messageSender = new MessageSender(gson);

        messageReceiver.receive(Constants.Queues.OPEN_ORDERS, (Order order) -> {
            try {
                if (strategy.needsApproval(order)) {
                    messageSender.send(Constants.Queues.NEED_APPROVAL, order);
                    StaticLogger.logger.info("order is too expensive and needs approval of 'Teamleitung'\n");
                } else {
                    order.setApprovedBy("Buchhaltung");
                    messageSender.send(Constants.Queues.APPROVED_ORDERS, order);
                    StaticLogger.logger.info("order was approved by 'Buchhaltung'\n");
                }
            } catch (TimeoutException | IOException e) {
                StaticLogger.logException(e);
            }
        }, Order.class);
    }
}
