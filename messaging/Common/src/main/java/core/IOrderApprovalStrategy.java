package core;

import domain.models.Order;

/**
 * Interface for ApprovalStrategy
 */
public interface IOrderApprovalStrategy {
    public boolean needsApproval(Order order);
}
