package domain.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {

    private static Gson gson = new Gson();

    @Test
    public void equals1() {
        Order order = new Order(100, new Customer("", "", "", ""));

        JsonElement jsonElement = gson.toJsonTree(order);
        JsonObject jsonObject = (JsonObject) jsonElement;
        jsonObject.addProperty("orderId", order.getOrderId().toString());

        Order orderSame = gson.fromJson(jsonObject, Order.class);
        Order orderDifferent = new Order(100, new Customer("", "", "", ""));

        assertEquals(order, orderSame);
        assertNotEquals(order, orderDifferent);
    }
}