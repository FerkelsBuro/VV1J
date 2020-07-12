package th.vv3.marketingService;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import core.loggers.StaticLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import th.vv3.DTOs.CustomerAccount;
import th.vv3.DTOs.Email;
import th.vv3.DTOs.EmailResponse;
import th.vv3.proxies.CustomerProxy;
import th.vv3.proxies.EmailProxy;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MailSpamThread implements Runnable {
    private final static Duration INTERVAL = Duration.ofMinutes(5);

    private static Logger mailStatusLogger = Logger.getLogger("MyLog");

    static {
        try {
            FileHandler fh = new FileHandler("logs" + File.separator + "Email.log");
            mailStatusLogger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private final Gson gson = new Gson();

    private EmailProxy emailProxy;
    private CustomerProxy customerProxy;

    public MailSpamThread(EmailProxy emailProxy, CustomerProxy customerProxy) {
        this.emailProxy = emailProxy;
        this.customerProxy = customerProxy;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            UUID emailId = emailProxy.spamCustomers(customerProxy.getAllAccounts());
            mailStatusLogger.info("Status of Email " + emailId + ": " + emailProxy.getStatusOfEmail(emailId));
            try {
                Thread.sleep(INTERVAL.toMillis());
            } catch (InterruptedException e) {
                StaticLogger.logException(e);
            }
        }
    }


}
