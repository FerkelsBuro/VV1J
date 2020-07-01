package th.vv3.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private UUID paymentId;
    private UUID orderId;
    private UUID customerId;
    private int amount;
}
