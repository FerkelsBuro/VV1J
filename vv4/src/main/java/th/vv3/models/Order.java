package th.vv3.models;

import th.vv3.DTOs.OrderReadDto;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Order with an UUID
 */
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @Column(unique = true)
    private UUID orderId;
    private int amount;
    private Date createDate;
    private String approvedBy;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    public Order() {
    }

    public Order(OrderReadDto dto) {
        if(dto.getOrderId() == null) {
            orderId = UUID.randomUUID();
        } else {
            orderId = dto.getOrderId();
        }
        amount = dto.getAmount();
        createDate = dto.getCreateDate();
        approvedBy = dto.getApprovedBy();
    }

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

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
