package th.vv3.models;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.UUID;


/**
 * Class for a Customer with UUID, salutation, firstName, LastName and an email-address
 */
@Entity
public class Customer {
    @Id
    @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID customerId;
    @ApiModelProperty(example = "Unknown")
    private String salutation;
    @ApiModelProperty(example = "Michael")
    private String firstName;
    @ApiModelProperty(example = "Backer")
    private String lastName;
    @Column(unique = true)
    @ApiModelProperty(example = "MichaelBacker@gmail.com")
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

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
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
