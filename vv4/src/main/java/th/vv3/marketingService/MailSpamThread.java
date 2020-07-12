package th.vv3.marketingService;

import core.loggers.StaticLogger;
import th.vv3.proxies.CustomerProxy;
import th.vv3.proxies.EmailProxy;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
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
