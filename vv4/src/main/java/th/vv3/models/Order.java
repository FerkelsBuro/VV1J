package th.vv3.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Order with an UUID
 */
@Entity
@Table(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;
    private int amount;
    private Date createDate;
    @OneToOne(cascade = CascadeType.ALL)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
