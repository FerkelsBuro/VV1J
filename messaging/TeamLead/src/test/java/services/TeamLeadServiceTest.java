package services;

import com.google.gson.Gson;
import core.Constants;
import core.IOrderApprovalStrategy;
import core.OrderApprovalStrategy;
import domain.models.Customer;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import org.junit.Before;
import org.junit.Test;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

//@PrepareForTest // Static.class contains static methods
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

        verify(mockMessageSender).send(Constants.Queues.APPROVED_ORDERS, order);
    }

    @Test
    public void orderResponse_OrderGetsNotApproved() throws IOException, TimeoutException {
        when(strategy.needsApproval(any(Order.class))).thenReturn(false);

        Order order = new Order(1000, new Customer("", "", "", ""));

        teamLeadService.orderResponse(order);

        verify(mockMessageSender).send(Constants.Queues.DECLINED_ORDER, order);
    }

//    @Test
//    public void watchOpenOrders() throws Exception {
//        String channel = Constants.Queues.NEED_APPROVAL;
//        Order order = new Order(100, new Customer("", "", "", ""));
//
//        Gson gson = new Gson();
//
//        when(strategy.needsApproval(any(Order.class))).thenReturn(true);
//
//        MessageReceiver messageReceiver = new MessageReceiver(gson);
//        MessageSender messageSender = new MessageSender(gson);
//        messageSender.send(channel, order);
//
//        TeamLeadService teamLeadService = new TeamLeadService(messageReceiver, mockMessageSender, strategy);
//
//        Thread thread = new Thread(teamLeadService);
//        thread.start();
//        Thread.sleep(100);
//        thread.interrupt();
//
//        verify(mockMessageSender).send(Constants.Queues.APPROVED_ORDERS, order);
//    }
}