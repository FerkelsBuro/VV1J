package th.vv3.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private UUID paymentId;
    private UUID orderId;
    private UUID customerId;
    private int amount;

    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public int getAmount() {
        return amount;
    }
}
