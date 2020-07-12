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

public class EmailProxy {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN0dWR3aWVyYWw1MjQwIiwibmJmIjoxNTk0MTIyMzUyLCJleHAiOjE1OTQ3MjcxNTIsImlhdCI6MTU5NDEyMjM1Mn0.KThLzJY235gaD-yHeQg0v9fs0n5f2y-wuX53N3xTWWg";
    private static final Gson gson = new Gson();
    private final WebClient client = WebClient.create("https://vvdemomailserviceprovider.azurewebsites.net");

    public UUID spamCustomers(List<CustomerAccount> customers) {
        Email email = new Email(customers, "buy new product!!");

        WebClient.RequestHeadersSpec<?> uri = client.post()
                .uri("api/v1/Email")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN))
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
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        String response = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        StaticLogger.logger.info(response);

        EmailResponse emailResponse = gson.fromJson(response, EmailResponse.class);
        return emailResponse.getEmailStatus();
    }
}
