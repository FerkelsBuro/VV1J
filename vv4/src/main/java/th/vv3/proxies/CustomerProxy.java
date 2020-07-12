package th.vv3.proxies;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.loggers.StaticLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import th.vv3.DTOs.CustomerAccount;
import th.vv3.models.Customer;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class CustomerProxy {
    private final Gson gson;
    private final WebClient client;
    private Supplier<String> getToken;

    public CustomerProxy(Gson gson, WebClient client, Supplier<String> getToken) {
        this.gson = gson;
        this.client = client;
        this.getToken = getToken;
    }

    public void createAccount(Customer customer) {
        WebClient.RequestHeadersSpec<?> uri = client.post()
                .uri("api/v1/Account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()))
                .body(Mono.just(gson.toJson(new CustomerAccount(customer))), String.class);

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);
    }

    public void deleteCustomer(Customer customer) {
        WebClient.RequestHeadersSpec<?> uri = client.delete()
                .uri("api/v1/Account/" + customer.getCustomerId())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()));

        HttpStatus response = Objects.requireNonNull(uri.exchange()
                .block())
                .statusCode();

        StaticLogger.logger.info(String.valueOf(response));
    }

    public CustomerAccount getAccountById(UUID id) {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Account/" + id)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        return gson.fromJson(response, CustomerAccount.class);
    }

    public List<CustomerAccount> getAllAccounts() {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Account")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        return gson.fromJson(response, new TypeToken<List<CustomerAccount>>() {
        }.getType());
    }
}
