package core;

import domain.models.Order;

/**
 * An extremely complex algorithm that decides whether an Order gets approved or not
 */
public class OrderApprovalStrategy implements IOrderApprovalStrategy {
    public boolean needsApproval(Order order) {
        return Math.random() > 0.5;
    }
}
