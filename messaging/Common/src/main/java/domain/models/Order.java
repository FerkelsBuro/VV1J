package domain.models;

import java.util.Date;
import java.util.UUID;

public class Order {
    private UUID orderId;
    private int amount;
    private Date createDate;
    private Customer customer;
    private String approvedBy;

    public Order(int amount, Customer customer) {
        orderId = UUID.randomUUID();
        this.amount = amount;
        this.customer = customer;
        createDate = new Date(System.currentTimeMillis());
    }

    public int getAmount() {
        return amount;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}

