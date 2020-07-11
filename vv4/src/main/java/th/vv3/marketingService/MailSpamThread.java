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

    private final WebClient client;
    private final Gson gson = new Gson();

    private Supplier<List<CustomerAccount>> getCustomers;

    public MailSpamThread(WebClient client, Supplier<List<CustomerAccount>> getCustomers) {
        this.client = client;
        this.getCustomers = getCustomers;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            UUID emailId = spamCustomers(getCustomers.get());
            mailStatusLogger.info("Status of Email " + emailId + ": " + getStatusOfEmail(emailId));
            try {
                Thread.sleep(INTERVAL.toMillis());
            } catch (InterruptedException e) {
                StaticLogger.logException(e);
            }
        }
    }

    private UUID spamCustomers(List<CustomerAccount> customers) {
        Email email = new Email(customers, "buy new product!!");

        WebClient.RequestHeadersSpec<?> uri = client.post()
                .uri("api/v1/Email")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(MarketingService.TOKEN))
                .body(Mono.just(gson.toJson(email)), String.class);

        HttpStatus response = Objects.requireNonNull(uri.exchange()
                .block())
                .statusCode();

        StaticLogger.logger.info(String.valueOf(response));

        return email.getEmailID();
    }

    private String getStatusOfEmail(UUID emailId) {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Email/" + emailId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(MarketingService.TOKEN));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        EmailResponse emailResponse = gson.fromJson(response, EmailResponse.class);
        return emailResponse.getEmailStatus();
    }
}
