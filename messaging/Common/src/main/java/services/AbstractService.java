package services;

import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractService {
    protected MessageReceiver messageReceiver;
    protected MessageSender messageSender;

    public AbstractService(MessageReceiver messageReceiver, MessageSender messageSender) {
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
    }

    public void watchOpenOrders() throws IOException, TimeoutException {
        messageReceiver.receive(getChannel(), (Order order) -> {
            try {
                orderResponse(order);
            } catch (IOException | TimeoutException e) {
                StaticLogger.logException(e);
            }
        }, Order.class);
    }

    public abstract void orderResponse(Order order) throws IOException, TimeoutException;

    public abstract String getChannel();
}
