package services;

import core.Constants;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CustomerService extends AbstractService {
    public CustomerService(MessageReceiver messageReceiver, MessageSender messageSender) {
        super(messageReceiver, messageSender, null, null, Constants.Queues.OPEN_ORDERS);
    }

    public <T> void sendOrder(T message) throws IOException, TimeoutException {
        send(message);
    }

    @Override
    public void run() {

    }
}
