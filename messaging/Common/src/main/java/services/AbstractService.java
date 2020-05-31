package services;

import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractService {
    protected final String receiveChannel;
    protected final String sendChannel;
    protected MessageReceiver messageReceiver;
    protected MessageSender messageSender;
    protected IOrderApprovalStrategy strategy;

    public AbstractService(MessageReceiver messageReceiver, MessageSender messageSender,
                           IOrderApprovalStrategy strategy, String receiveChannel, String sendChannel) {
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
        this.strategy = strategy;
        this.receiveChannel = receiveChannel;
        this.sendChannel = sendChannel;
    }

    public void watchChannel() throws IOException, TimeoutException {
        messageReceiver.receive(receiveChannel, order -> {
            try {
                handleMessage(order);
            } catch (IOException | TimeoutException e) {
                StaticLogger.logException(e);
            }
        }, Order.class);
    }

    public <T> void send(T message) throws IOException, TimeoutException {
        messageSender.send(sendChannel, message);
    }

    abstract public void handleMessage(Order order) throws IOException, TimeoutException;
}
