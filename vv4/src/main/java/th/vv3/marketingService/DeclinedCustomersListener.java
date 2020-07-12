package th.vv3.marketingService;

import core.Constants;
import core.loggers.StaticLogger;
import infrastructure.MessageReceiver;
import th.vv3.models.Customer;
import th.vv3.proxies.CustomerProxy;

import java.io.IOException;

public class DeclinedCustomersListener implements Runnable {
    private MessageReceiver messageReceiver;
    private CustomerProxy customerProxy;

    public DeclinedCustomersListener(CustomerProxy customerProxy, MessageReceiver messageReceiver) {
        this.customerProxy = customerProxy;
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void run() {
        try {
            messageReceiver.receive(Constants.Exchanges.DECLINED_CUSTOMERS, Constants.Queues.DECLINED_CUSTOMERS,
                    customerProxy::deleteCustomer, Customer.class);
        } catch (IOException e) {
            StaticLogger.logException(e);
        }
    }
}
