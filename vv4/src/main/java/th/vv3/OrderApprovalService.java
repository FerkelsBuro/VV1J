package th.vv3;

import com.google.gson.Gson;
import core.Constants;
import core.loggers.StaticLogger;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import th.vv3.DTOs.OrderReadDto;
import th.vv3.controller.v1.CustomersController;
import th.vv3.controller.v1.OrdersController;
import th.vv3.controller.v1.PaymentsController;
import th.vv3.models.Customer;
import th.vv3.models.Order;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class OrderApprovalService implements CommandLineRunner {
    private static Gson gson = new Gson();
    private MessageReceiver messageReceiver = new MessageReceiver(gson);
    private MessageSender messageSender = new MessageSender(gson);

    private CustomersController customersController;
    private OrdersController ordersController;
    private PaymentsController paymentsController;

    @Autowired
    public OrderApprovalService(CustomersController customersController, OrdersController ordersController, PaymentsController paymentsController) throws IOException, TimeoutException {
        this.customersController = customersController;
        this.ordersController = ordersController;
        this.paymentsController = paymentsController;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApprovalService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        messageReceiver.receive(Constants.Exchanges.APPROVED_ORDERS, Constants.Queues.APPROVED_ORDERS,
                order -> {
                    try {
                        orderResponse(order);
                    } catch (IOException | TimeoutException e) {
                        StaticLogger.logException(e);
                    }
                }, Order.class);
    }

    private void orderResponse(Order order) throws IOException, TimeoutException {
        System.out.println("received: " + gson.toJson(order));
        Customer customer = order.getCustomer();

        ResponseEntity amount = paymentsController.getAmountByCustomerId(customer.getCustomerId());
        if (amount.getStatusCode() == HttpStatus.NOT_FOUND
                || amount.getStatusCode() == HttpStatus.OK && Integer.parseInt(Objects.requireNonNull(amount.getBody()).toString()) < 1000) {

            if (amount.getStatusCode() == HttpStatus.NOT_FOUND) {
                ResponseEntity responseEntity = customersController.create(customer);
                System.out.println("response: " + responseEntity);
            }
            order.setOrderId(null);
            ResponseEntity responseEntity = ordersController.create(new OrderReadDto(order), customer.getCustomerId());
            System.out.println("response: " + responseEntity);

            messageSender.send(Constants.Exchanges.APPROVED_CUSTOMERS, customer);
            System.out.println();

        } else {
            messageSender.send(Constants.Exchanges.DECLINED_CUSTOMERS, customer);
            System.out.println("Customer was declined\n");
        }


    }
}
