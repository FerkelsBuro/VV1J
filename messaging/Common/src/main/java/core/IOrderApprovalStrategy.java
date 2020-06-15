package core;

import domain.models.Order;

/**
 * Interface for ApprovalStrategy
 */
@FunctionalInterface
public interface IOrderApprovalStrategy {
    public boolean needsApproval(Order order);
}
