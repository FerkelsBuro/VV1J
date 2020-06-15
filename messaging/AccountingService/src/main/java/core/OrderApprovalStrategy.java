package core;

import domain.models.Order;

/**
 * Decides if an Order gets approved or not
 */
public class OrderApprovalStrategy implements IOrderApprovalStrategy {
    public boolean needsApproval(Order order) {
        return order.getAmount() >= 500;
    }
}
