package services;

import core.Constants;
import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AccountingService extends AbstractService {
    public AccountingService(MessageReceiver messageReceiver, MessageSender messageSender, IOrderApprovalStrategy strategy) {
        super(messageReceiver, messageSender, strategy, Constants.Queues.OPEN_ORDERS, Constants.Queues.NEED_APPROVAL);
    }

    public void watchOpenOrders() throws IOException, TimeoutException {
        watchChannel(order -> {
            try {
                handleMessage(order);
            } catch (IOException | TimeoutException e) {
                StaticLogger.logException(e);
            }
        });
    }

    public void handleMessage(Order order) throws IOException, TimeoutException {
        if (strategy.needsApproval(order)) {
            messageSender.send(receiveChannel, order);
            StaticLogger.logger.info("order is too expensive and needs approval of 'Teamleitung'\n");
        } else {
            order.setApprovedBy("Buchhaltung");
            messageSender.send(receiveChannel, order);
            StaticLogger.logger.info("order was approved by 'Buchhaltung'\n");
        }
    }

    @Override
    public void run() {

    }
}
