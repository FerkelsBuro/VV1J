package services;

import domain.models.Customer;
import domain.models.Order;

import java.util.concurrent.ThreadLocalRandom;

public class RandomOrderGenerator {
    private static final int MINIMUM_AMOUNT = 1;
    private static final int MAXIMUM_AMOUNT = 2_000;

    public static Order createRandomOrder() {
        Customer customer = RandomCustomerGenerator.createRandomCustomer();
        return new Order(ThreadLocalRandom.current().nextInt(MINIMUM_AMOUNT, MAXIMUM_AMOUNT), customer);
    }


}
