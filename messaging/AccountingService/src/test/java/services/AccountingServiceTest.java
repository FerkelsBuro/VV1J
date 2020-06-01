package services;

import core.Constants;
import core.OrderApprovalStrategy;
import domain.models.Customer;
import domain.models.Order;
import infrastructure.MessageSender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountingServiceTest {
    private static OrderApprovalStrategy strategy;
    private static MessageSender mockMessageSender;

    private static AccountingService accountingService;

    @Before
    public void setup() {
        strategy = new OrderApprovalStrategy();
        mockMessageSender = mock(MessageSender.class);

        accountingService = new AccountingService(null, mockMessageSender, strategy);
    }

    @Test
    public void orderResponse_OrderGetsApproved() throws IOException, TimeoutException {
        Order order = new Order(100, new Customer("", "", "", ""));

        accountingService.orderResponse(order);

        verify(mockMessageSender).send(Constants.Queues.APPROVED_ORDERS, order);
    }

    @Test
    public void orderResponse_OrderGetsNotApproved() throws IOException, TimeoutException {
        Order order = new Order(1000, new Customer("", "", "", ""));

        accountingService.orderResponse(order);

        verify(mockMessageSender).send(Constants.Queues.NEED_APPROVAL, order);
    }
}