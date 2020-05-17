package core;

import domain.models.Order;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderApprovalStrategyTest {
    private OrderApprovalStrategy strategy = new OrderApprovalStrategy();

    @Test
    public void checkApproval_SmallOrderAmount() {
        Order order = new Order(10, null);
        assertFalse(strategy.needsApproval(order));
    }

    @Test
    public void checkApproval_BigOrderAmount() {
        Order order = new Order(10_000, null);
        assertTrue(strategy.needsApproval(order));
    }

    @Test
    public void checkApproval_MediumOrderAmount() {
        Order order = new Order(500, null);
        assertTrue(strategy.needsApproval(order));
    }
}