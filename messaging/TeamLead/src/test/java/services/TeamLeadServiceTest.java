package services;

import core.Constants;
import core.IOrderApprovalStrategy;
import domain.models.Customer;
import domain.models.Order;
import infrastructure.MessageSender;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class TeamLeadServiceTest {

    private static IOrderApprovalStrategy strategy;
    private static MessageSender mockMessageSender;

    private static TeamLeadService teamLeadService;

    @Before
    public void setup() {
        mockMessageSender = mock(MessageSender.class);
        strategy = mock(IOrderApprovalStrategy.class);
        teamLeadService = new TeamLeadService(null, mockMessageSender, strategy);
    }

    @Test
    public void orderResponse_OrderGetsApproved() throws IOException, TimeoutException {
        when(strategy.needsApproval(any(Order.class))).thenReturn(true);

        Order order = new Order(100, new Customer("", "", "", ""));

        teamLeadService.orderResponse(order);

        verify(mockMessageSender).send(Constants.Exchanges.APPROVED_ORDERS, order);
    }

    @Test
    public void orderResponse_OrderGetsNotApproved() throws IOException, TimeoutException {
        when(strategy.needsApproval(any(Order.class))).thenReturn(false);

        Order order = new Order(1000, new Customer("", "", "", ""));

        teamLeadService.orderResponse(order);

        verify(mockMessageSender).send(Constants.Exchanges.DECLINED_ORDER, order);
    }
}