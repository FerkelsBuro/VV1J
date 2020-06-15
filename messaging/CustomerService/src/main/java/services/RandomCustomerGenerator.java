package services;

import com.github.javafaker.Faker;
import domain.models.Customer;

/**
 * Allows to generate random Customers
 */
public class RandomCustomerGenerator {
    private static Faker faker = new Faker();

    private RandomCustomerGenerator() {
    }

    public static Customer createRandomCustomer() {
        String salutation = faker.name().prefix();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "@" + lastName + ".com";

        return new Customer(salutation, firstName, lastName, email);
    }
}
