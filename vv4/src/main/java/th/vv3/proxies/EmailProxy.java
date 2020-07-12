package th.vv3.proxies;

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

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class EmailProxy {
    private final Gson gson;
    private final WebClient client;
    private Supplier<String> getToken;

    public EmailProxy(Gson gson, WebClient client, Supplier<String> getToken) {
        this.gson = gson;
        this.client = client;
        this.getToken = getToken;
    }

    public UUID spamCustomers(List<CustomerAccount> customers) {
        Email email = new Email(customers, "buy new product!!");

        WebClient.RequestHeadersSpec<?> uri = client.post()
                .uri("api/v1/Email")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()))
                .body(Mono.just(gson.toJson(email)), String.class);

        HttpStatus response = Objects.requireNonNull(uri.exchange()
                .block())
                .statusCode();

        StaticLogger.logger.info(String.valueOf(response));

        return email.getEmailID();
    }

    public String getStatusOfEmail(UUID emailId) {
        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/Email/" + emailId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken.get()));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        EmailResponse emailResponse = gson.fromJson(response, EmailResponse.class);
        return emailResponse.getEmailStatus();
    }
}
