package th.vv3.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailResponse {
    private Email email;
    private String emailStatus;
    private String additionalInformation;

    @JsonProperty("email")
    public Email getEmail() { return email; }
    @JsonProperty("email")
    public void setEmail(Email value) { this.email = value; }

    @JsonProperty("emailStatus")
    public String getEmailStatus() { return emailStatus; }
    @JsonProperty("emailStatus")
    public void setEmailStatus(String value) { this.emailStatus = value; }

    @JsonProperty("additionalInformation")
    public String getAdditionalInformation() { return additionalInformation; }
    @JsonProperty("additionalInformation")
    public void setAdditionalInformation(String value) { this.additionalInformation = value; }
}
