package services;

import com.google.gson.Gson;
import core.Constants;
import core.OrderApprovalStrategy;
import domain.models.Customer;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountingServiceTest {
    private static final Gson gson = new Gson();

//    @Test
//    public void watchOpenOrders() throws IOException, TimeoutException, InterruptedException {
//        MessageSender messageSender = new MessageSender(gson);
//        MessageReceiver messageReceiver = new MessageReceiver(gson);
//        OrderApprovalStrategy strategy = new OrderApprovalStrategy();
//        MessageSender mockMessageSender = mock(MessageSender.class);
//
//        AccountingService accountingService = new AccountingService(messageReceiver, mockMessageSender, strategy);
//
//        Order order = new Order(100, new Customer("", "", "", ""));
//
//        Thread runnable = new Thread(() -> {
//            try {
//                accountingService.watchOpenOrders();
//            } catch (IOException | TimeoutException ignored) {
//            }
//        });
//
//        messageSender.send(Constants.Queues.OPEN_ORDERS, order);
//
//        runnable.run();
//        Thread.sleep(100);
//        runnable.interrupt();
//
//        verify(mockMessageSender).send(Constants.Queues.APPROVED_ORDERS, order);
//    }

    @Test
    public void sendOrderResponse() throws IOException, TimeoutException {
        OrderApprovalStrategy strategy = new OrderApprovalStrategy();
        MessageSender mockMessageSender = mock(MessageSender.class);

        AccountingService accountingService = new AccountingService(null, mockMessageSender, strategy);

        Order order = new Order(100, new Customer("", "", "", ""));

        accountingService.sendOrderResponse(order);

        verify(mockMessageSender).send(Constants.Queues.APPROVED_ORDERS, order);
    }
}