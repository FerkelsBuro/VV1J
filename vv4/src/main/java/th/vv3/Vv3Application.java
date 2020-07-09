package th.vv3;

import com.google.gson.Gson;
import core.Constants;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import th.vv3.controller.CustomersController;
import th.vv3.controller.PaymentsController;
import th.vv3.models.Customer;
import th.vv3.models.Order;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class Vv3Application implements InitializingBean, CommandLineRunner {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN0dWR3aWVyYWw1MjQwIiwibmJmIjoxNTk0MTIyMzUyLCJleHAiOjE1OTQ3MjcxNTIsImlhdCI6MTU5NDEyMjM1Mn0.KThLzJY235gaD-yHeQg0v9fs0n5f2y-wuX53N3xTWWg";
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Vv3Application.class);
    private static WebClient client = WebClient.create("http://localhost:8080");
//    private static WebClient client = WebClient.create("https://vvdemomailserviceprovider.azurewebsites.net");
    private static Gson gson = new Gson();
    private MessageReceiver messageReceiver = new MessageReceiver(gson);
    private MessageSender messageSender = new MessageSender(gson);
    private CustomersController customersController;
    private PaymentsController paymentsController;

    public Vv3Application() throws IOException, TimeoutException {
        LOG.info("Constructor");
    }

    @Autowired
    public Vv3Application(CustomersController customersController, PaymentsController paymentsController) throws IOException, TimeoutException {
        this.customersController = customersController;
        this.paymentsController = paymentsController;
    }

    public static void main(String[] args) {
        SpringApplication.run(Vv3Application.class, args);

        WebClient.RequestHeadersSpec<?> uri = client.get()
                .uri("api/v1/customers")
//                .uri("api/v1/Email?limit=5")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(TOKEN));

        String response2 = Objects.requireNonNull(uri.exchange()
                .block())
                .bodyToMono(String.class)
                .block();
        System.out.println(response2);
    }

    @Override
    public void afterPropertiesSet() {
        LOG.info("InitializingBean");
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("PostConstruct");
    }

    @Override
    public void run(String... args) throws Exception {
        messageReceiver.receive(Constants.Exchanges.APPROVED_ORDERS, Constants.Queues.APPROVED_ORDERS,
                this::orderResponse, Order.class);
    }

    private void orderResponse(Order order) {
        System.out.println("received: " + gson.toJson(order));
        Customer customer = order.getCustomer();

        ResponseEntity amount = paymentsController.getAmountByCustomerId(customer.getCustomerId());
        if (amount.getStatusCode() == HttpStatus.NOT_FOUND
                || amount.getStatusCode() == HttpStatus.OK && Integer.parseInt(Objects.requireNonNull(amount.getBody()).toString()) < 1000) {
            customer.setCustomerId(null);
            ResponseEntity responseEntity = customersController.create(customer);
            System.out.println("response: " + responseEntity + "\n");
        } else {
            System.out.println("Customer was declined\n");
        }


    }
}
