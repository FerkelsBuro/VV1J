package core;

import domain.models.Order;

public class OrderApprovalStrategy {
    public boolean getsApproval(Order order) {
        return Math.random() > 0.5;
    }
}
