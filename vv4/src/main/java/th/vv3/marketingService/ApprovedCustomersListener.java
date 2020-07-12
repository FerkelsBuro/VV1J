package th.vv3.marketingService;

import core.Constants;
import core.loggers.StaticLogger;
import infrastructure.MessageReceiver;
import th.vv3.models.Customer;
import th.vv3.proxies.CustomerProxy;

import java.io.IOException;

public class ApprovedCustomersListener implements Runnable {
    private CustomerProxy customerProxy;
    private MessageReceiver messageReceiver;

    public ApprovedCustomersListener(CustomerProxy customerProxy, MessageReceiver messageReceiver) {
        this.customerProxy = customerProxy;
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void run() {
        try {
            messageReceiver.receive(Constants.Exchanges.APPROVED_CUSTOMERS, Constants.Queues.APPROVED_CUSTOMERS,
                    customerProxy::createAccount, Customer.class);
        } catch (IOException e) {
            StaticLogger.logException(e);
        }
    }
}
