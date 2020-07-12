package th.vv3.DTOs;

import th.vv3.models.Order;

import java.util.Date;
import java.util.UUID;

public class OrderReadDto {
    private UUID orderId;
    private int amount;
    private Date createDate;
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
