package th.vv3.models;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    @ApiModelProperty(example = "4fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID paymentId;
    @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID orderId;
    @ManyToOne
    private Order order;
    @ApiModelProperty(example = "2fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID customerId;
    @ApiModelProperty(example = "50")
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

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
