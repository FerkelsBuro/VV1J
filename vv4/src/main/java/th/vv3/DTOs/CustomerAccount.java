package th.vv3.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import th.vv3.models.Customer;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerAccount {
    private UUID accountId;
    private String salutation;
    private String firstName;
    private String lastName;
    private String email;
    private String createDate;

    public CustomerAccount() {
    }

    /**
     * Converts a Customer to a CustomerAccount
     * Salutation is assumed as "Unknown" since the Mail-provider can't handle the salutations of Customer
     *
     * @param customer
     */
    public CustomerAccount(Customer customer) {
        this.accountId = customer.getCustomerId();
        this.salutation = "Unknown";
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
    }

    @JsonProperty("accountId")
    public UUID getAccountId() {
        return accountId;
    }

    @JsonProperty("accountId")
    public void setAccountId(UUID value) {
        this.accountId = value;
    }

    @JsonProperty("salutation")
    public String getSalutation() {
        return salutation;
    }

    @JsonProperty("salutation")
    public void setSalutation(String value) {
        this.salutation = value;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String value) {
        this.firstName = value;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String value) {
        this.lastName = value;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String value) {
        this.email = value;
    }

    @JsonProperty("createDate")
    public String getCreateDate() {
        return createDate;
    }

    @JsonProperty("createDate")
    public void setCreateDate(String value) {
        this.createDate = value;
    }
}