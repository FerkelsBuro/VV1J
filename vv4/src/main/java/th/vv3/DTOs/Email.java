package th.vv3.DTOs;

import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class Email {
    private UUID emailID;
    private List<CustomerAccount> recipients;
    private String message;

    public Email() {
    }

    public Email(List<CustomerAccount> recipients, String message) {
        this.emailID = UUID.randomUUID();
        this.recipients = recipients;
        this.message = message;
    }

    @JsonProperty("emailId")
    public UUID getEmailID() { return emailID; }
    @JsonProperty("emailId")
    public void setEmailID(UUID value) { this.emailID = value; }

    @JsonProperty("recipients")
    public List<CustomerAccount> getRecipients() { return recipients; }
    @JsonProperty("recipients")
    public void setRecipients(List<CustomerAccount> value) { this.recipients = value; }

    @JsonProperty("message")
    public String getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(String value) { this.message = value; }

}
