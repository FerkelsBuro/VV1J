package services;

import com.github.javafaker.Faker;
import domain.models.Customer;

public class RandomCustomerGenerator {
    private static Faker faker = new Faker();

    public static Customer createRandomCustomer() {
        String salutation = faker.name().prefix();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "@" + lastName + ".com";

        return new Customer(salutation, firstName, lastName, email);
    }
}
