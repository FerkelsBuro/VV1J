package services;

import core.Constants;
import core.OrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AccountingService {
    private MessageReceiver messageReceiver;
    private MessageSender messageSender;
    private OrderApprovalStrategy strategy;

    public AccountingService(MessageReceiver messageReceiver, MessageSender messageSender, OrderApprovalStrategy strategy) {
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
        this.strategy = strategy;
    }

    public void watchOpenOrders() throws IOException, TimeoutException {
        messageReceiver.receive(Constants.Queues.OPEN_ORDERS, (Order order) -> {
            try {
                sendOrderResponse(order);
            } catch (IOException | TimeoutException e) {
                StaticLogger.logException(e);
            }
        }, Order.class);

    }

    public void sendOrderResponse(Order order) throws IOException, TimeoutException {
        if (strategy.needsApproval(order)) {
            messageSender.send(Constants.Queues.NEED_APPROVAL, order);
            StaticLogger.logger.info("order is too expensive and needs approval of 'Teamleitung'\n");
        } else {
            order.setApprovedBy("Buchhaltung");
            messageSender.send(Constants.Queues.APPROVED_ORDERS, order);
            StaticLogger.logger.info("order was approved by 'Buchhaltung'\n");
        }
    }
}
