package th.vv3.marketingService;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.loggers.StaticLogger;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import th.vv3.DTOs.CustomerAccount;
import th.vv3.models.Customer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

// LW: Alle HTTP aufrufe in seperate Klasse aufteilen
// LW: z.B. api.AccountClient, api.CustomerClient
public class MarketingService {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN0dWR3aWVyYWw1MjQwIiwibmJmIjoxNTk0MTIyMzUyLCJleHAiOjE1OTQ3MjcxNTIsImlhdCI6MTU5NDEyMjM1Mn0.KThLzJY235gaD-yHeQg0v9fs0n5f2y-wuX53N3xTWWg";
    //    private static WebClient client = WebClient.create("http://localhost:8080");
    private static final WebClient client = WebClient.create("https://vvdemomailserviceprovider.azurewebsites.net");

    private static final Gson gson = new Gson();
    private MessageReceiver messageReceiver = new MessageReceiver(gson);
    private MessageSender messageSender = new MessageSender(gson);

    public MarketingService() throws IOException, TimeoutException {
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        MarketingService marketingService = new MarketingService();

        Thread approvedCustomersListener = new Thread(new ApprovedCustomersListener(marketingService::createAccount, marketingService.messageReceiver));
        approvedCustomersListener.start();

        Thread declinedCustomersListener = new Thread(new DeclinedCustomersListener(marketingService::deleteCustomer, marketingService.messageReceiver));
        declinedCustomersListener.start();
    }

    private void createAccount(Customer customer) {
        WebClient.RequestHeadersSpec<?> uri = client.post()
                .uri("api/v1/Account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN))
                .body(Mono.just(gson.toJson(new CustomerAccount(customer))), String.class);

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);
    }

    private void deleteCustomer(Customer customer) {
        WebClient.RequestHeadersSpec<?> uri = client.delete()
                .uri("api/v1/Account/" + customer.getCustomerId())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        HttpStatus response = Objects.requireNonNull(uri.exchange()
                .block())
                .statusCode();

        StaticLogger.logger.info(String.valueOf(response));
    }

    private CustomerAccount getAccountById(UUID id) {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Account/" + id)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        return gson.fromJson(response, CustomerAccount.class);
    }

    private List<CustomerAccount> getAllAccounts() {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Account")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        return gson.fromJson(response, new TypeToken<List<CustomerAccount>>() {
        }.getType());
    }
}
