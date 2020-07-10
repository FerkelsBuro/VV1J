package th.vv3;

import com.google.gson.Gson;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.springframework.boot.SpringApplication;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class MarketingService {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN0dWR3aWVyYWw1MjQwIiwibmJmIjoxNTk0MTIyMzUyLCJleHAiOjE1OTQ3MjcxNTIsImlhdCI6MTU5NDEyMjM1Mn0.KThLzJY235gaD-yHeQg0v9fs0n5f2y-wuX53N3xTWWg";
//    private static WebClient client = WebClient.create("http://localhost:8080");
    private static WebClient client = WebClient.create("https://vvdemomailserviceprovider.azurewebsites.net");

    private static Gson gson = new Gson();
    private MessageReceiver messageReceiver = new MessageReceiver(gson);
    private MessageSender messageSender = new MessageSender(gson);

    public MarketingService() throws IOException, TimeoutException {
    }

    public static void main(String[] args) {
//        SpringApplication.run(OrderApprovalService.class, args);

        WebClient.RequestHeadersSpec<?> uri = client.get()
//                .uri("api/v1/customers")
                .uri("api/v1/Email?limit=5")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        String response2 = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        System.out.println(response2);
    }

}
