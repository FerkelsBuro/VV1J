package services;

import domain.models.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomCustomerGeneratorTest {

    @Test
    public void createRandomCustomer() {
        Customer customer = RandomCustomerGenerator.createRandomCustomer();
        assertNotNull(customer.getEmail());
        assertNotNull(customer.getCustomerId());
        assertNotNull(customer.getFirstName());
        assertNotNull(customer.getLastName());
        assertNotNull(customer.getSalutation());
    }
}