package domain.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void equals1() {
        Order order = new Order(100, new Customer("", "", "", ""));
        Order orderSame = order;
        Order orderDifferent = new Order(100, new Customer("", "", "", ""));

        assertEquals(order, orderSame);
        assertNotEquals(order, orderDifferent);
    }
}