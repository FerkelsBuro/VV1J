package services;

import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public abstract class AbstractService implements Runnable {
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

    protected void watchChannel(Consumer<Order> callBack) throws IOException, TimeoutException {
        messageReceiver.receive(receiveChannel, callBack, Order.class);
    }

    protected <T> void send(T message) throws IOException, TimeoutException {
        messageSender.send(sendChannel, message);
    }

//    abstract public void handleMessage(Order order) throws IOException, TimeoutException;
}
