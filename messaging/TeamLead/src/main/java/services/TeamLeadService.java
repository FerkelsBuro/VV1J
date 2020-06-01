package services;

import core.Constants;
import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TeamLeadService extends AbstractService implements Runnable {
    private IOrderApprovalStrategy strategy;

    public TeamLeadService(MessageReceiver messageReceiver, MessageSender messageSender, IOrderApprovalStrategy strategy) {
        super(messageReceiver, messageSender);
        this.strategy = strategy;
    }

    @Override
    public void orderResponse(Order order) throws IOException, TimeoutException {
        if (strategy.needsApproval(order)) {
            order.setApprovedBy("Teamleitung");
            messageSender.send(Constants.Queues.APPROVED_ORDERS, order);
            StaticLogger.logger.info("Teamlead was feeling happy and approved the order\n");
        } else {
            messageSender.send(Constants.Queues.DECLINED_ORDER, order);
            StaticLogger.logger.info("Teamlead wasn't feeling happy and didn't approve the order\n");
        }
    }

    @Override
    public String getChannel() {
        return Constants.Queues.NEED_APPROVAL;
    }

    @Override
    public void run() {
        try {
            watchOpenOrders();
        } catch (IOException | TimeoutException e) {
            StaticLogger.logException(e);
        }
    }
}

