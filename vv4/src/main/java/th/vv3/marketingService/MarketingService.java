package th.vv3.marketingService;

import com.google.gson.Gson;
import infrastructure.MessageReceiver;
import org.springframework.web.reactive.function.client.WebClient;
import th.vv3.proxies.CustomerProxy;
import th.vv3.proxies.EmailProxy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MarketingService {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN0dWR3aWVyYWw1MjQwIiwibmJmIjoxNTk0MTIyMzUyLCJleHAiOjE1OTQ3MjcxNTIsImlhdCI6MTU5NDEyMjM1Mn0.KThLzJY235gaD-yHeQg0v9fs0n5f2y-wuX53N3xTWWg";

    public static void main(String[] args) throws IOException, TimeoutException {
        Gson gson = new Gson();
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        WebClient webClient = WebClient.create("https://vvdemomailserviceprovider.azurewebsites.net");
        CustomerProxy customerProxy = new CustomerProxy(gson, webClient, MarketingService::getTOKEN);
        EmailProxy emailProxy = new EmailProxy(gson, webClient, MarketingService::getTOKEN);

        Thread approvedCustomersListener = new Thread(new ApprovedCustomersListener(customerProxy, messageReceiver));
        approvedCustomersListener.start();

        Thread declinedCustomersListener = new Thread(new DeclinedCustomersListener(customerProxy, messageReceiver));
        declinedCustomersListener.start();

        Thread mailSpanThread = new Thread(new MailSpamThread(emailProxy, customerProxy));
        mailSpanThread.start();
    }

    public static String getTOKEN() {
        return TOKEN;
    }
}
