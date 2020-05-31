package services;

import core.Constants;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

public class CustomerService extends AbstractService {
    public CustomerService(MessageReceiver messageReceiver, MessageSender messageSender) {
        super(messageReceiver, messageSender, null, null, Constants.Queues.OPEN_ORDERS);
    }

    @Override
    public void handleMessage(Order order) {
    }
}
