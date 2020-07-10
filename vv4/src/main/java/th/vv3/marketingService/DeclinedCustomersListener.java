package th.vv3.marketingService;

import core.Constants;
import core.loggers.StaticLogger;
import infrastructure.MessageReceiver;
import th.vv3.models.Customer;

import java.io.IOException;
import java.util.function.Consumer;

public class DeclinedCustomersListener implements Runnable {
    private Consumer<Customer> onEvent;
    private MessageReceiver messageReceiver;

    public DeclinedCustomersListener(Consumer<Customer> onEvent, MessageReceiver messageReceiver) {
        this.onEvent = onEvent;
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void run() {
        try {
            messageReceiver.receive(Constants.Exchanges.DECLINED_CUSTOMERS, Constants.Queues.DECLINED_CUSTOMERS,
                    onEvent, Customer.class);
        } catch (IOException e) {
            StaticLogger.logException(e);
        }
    }
}
