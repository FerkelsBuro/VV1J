package th.vv3.marketingService;

import com.google.gson.Gson;
import infrastructure.MessageReceiver;
import th.vv3.proxies.CustomerProxy;
import th.vv3.proxies.EmailProxy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MarketingService {
    public static void main(String[] args) throws IOException, TimeoutException {
        Gson gson = new Gson();
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        CustomerProxy customerProxy = new CustomerProxy();
        EmailProxy emailProxy = new EmailProxy();

        Thread approvedCustomersListener = new Thread(new ApprovedCustomersListener(customerProxy, messageReceiver));
        approvedCustomersListener.start();

        Thread declinedCustomersListener = new Thread(new DeclinedCustomersListener(customerProxy, messageReceiver));
        declinedCustomersListener.start();

        Thread mailSpanThread = new Thread(new MailSpamThread(emailProxy, customerProxy));
        mailSpanThread.start();
    }
}
