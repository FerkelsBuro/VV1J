package th.vv3.models;


import javax.persistence.*;
import java.util.UUID;


/**
 * Class for a Customer with UUID, salutation, firstName, LastName and an email-address
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private UUID customerId;
    private String salutation;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Version
    private Integer version;

    public Customer() {
    }


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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
