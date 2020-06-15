package services;

import core.Constants;
import core.OrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * The AccountingService receives message from "OpenOrders" and then sends it to "NeedApproval" or "ApprovedOrders"
 */
public class AccountingService extends AbstractService{
    private OrderApprovalStrategy strategy;

    public AccountingService(MessageReceiver messageReceiver, MessageSender messageSender, OrderApprovalStrategy strategy) {
        super(messageReceiver, messageSender);
        this.strategy = strategy;
    }

    @Override
    public void orderResponse(Order order) throws IOException, TimeoutException {
        if (strategy.needsApproval(order)) {
            messageSender.send(Constants.Exchanges.NEED_APPROVAL, order);
            StaticLogger.logger.info("order is too expensive and needs approval of 'Teamleitung'\n");
        } else {
            order.setApprovedBy("Buchhaltung");
            messageSender.send(Constants.Exchanges.APPROVED_ORDERS, order);
            StaticLogger.logger.info("order was approved by 'Buchhaltung'\n");
        }
    }

    @Override
    public String getExchange() {
        return Constants.Exchanges.OPEN_ORDERS;
    }

    @Override
    public String getQueue() {
        return Constants.Queues.OPEN_ORDERS;
    }
}

