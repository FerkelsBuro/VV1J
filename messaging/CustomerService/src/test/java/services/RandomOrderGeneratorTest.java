package services;

import domain.models.Order;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomOrderGeneratorTest {

    @Test
    public void createRandomOrder() {
        Order order = RandomOrderGenerator.createRandomOrder();
        assertNotNull(order.getCreateDate());
        assertNotNull(order.getOrderId());
        assertNotNull(order.getCustomer());
    }
}