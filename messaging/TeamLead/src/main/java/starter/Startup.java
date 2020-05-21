package starter;

import com.google.gson.Gson;
import core.Constants;
import core.OrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Startup {
    private static final OrderApprovalStrategy strategy = new OrderApprovalStrategy();
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws Exception {
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        MessageSender messageSender = new MessageSender(gson);

        messageReceiver.receive(Constants.Queues.NEED_APPROVAL, (Order order) -> {
            try {
                if (strategy.getsApproval(order)) {
                    order.setApprovedBy("Teamleitung");
                    messageSender.send(Constants.Queues.APPROVED_ORDERS, order);
                    StaticLogger.logger.info("Teamlead was feeling happy and approved the order\n");
                } else {
                    messageSender.send(Constants.Queues.DECLINED_ORDER, order);
                    StaticLogger.logger.info("Teamlead wasn't feeling happy and didn't approve the order\n");
                }
            } catch (TimeoutException | IOException e) {
                StaticLogger.logException(e);
            }
        }, Order.class);
    }
}
