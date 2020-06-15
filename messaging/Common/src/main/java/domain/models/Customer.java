package domain.models;

import java.util.UUID;

public class Customer {
    private UUID customerId;
    private String salutation;
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String salutation, String firstName, String lastName, String email) {
        customerId = UUID.randomUUID();
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
