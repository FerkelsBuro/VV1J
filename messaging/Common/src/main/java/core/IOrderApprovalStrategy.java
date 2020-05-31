package core;

import domain.models.Order;

public interface IOrderApprovalStrategy {
    public boolean needsApproval(Order order);
}
