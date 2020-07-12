package th.vv3.DTOs;

import io.swagger.annotations.ApiModelProperty;
import th.vv3.models.Order;

import java.util.Date;
import java.util.UUID;

public class OrderReadDto {
    @ApiModelProperty(example = "2fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID orderId;
    @ApiModelProperty(example = "100")
    private int amount;
    private Date createDate;
    @ApiModelProperty(example = "BOSS")
    private String approvedBy;

    public OrderReadDto(Order order) {
        orderId = order.getOrderId();
        amount = order.getAmount();
        createDate = order.getCreateDate();
        approvedBy = order.getApprovedBy();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
