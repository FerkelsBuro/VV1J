package services;

import core.Constants;
import core.IOrderApprovalStrategy;
import core.loggers.StaticLogger;
import domain.models.Order;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TeamLeadService extends AbstractService {
    private IOrderApprovalStrategy strategy;

    public TeamLeadService(MessageReceiver messageReceiver, MessageSender messageSender, IOrderApprovalStrategy strategy) {
        super(messageReceiver, messageSender);
        this.strategy = strategy;
    }

    @Override
    public void orderResponse(Order order) throws IOException, TimeoutException {
        if (strategy.needsApproval(order)) {
            order.setApprovedBy("Teamleitung");
            messageSender.send(Constants.Exchanges.APPROVED_ORDERS, order);
            StaticLogger.logger.info("Teamlead was feeling happy and approved the order\n");
        } else {
            messageSender.send(Constants.Exchanges.DECLINED_ORDER, order);
            StaticLogger.logger.info("Teamlead wasn't feeling happy and didn't approve the order\n");
        }
    }

    @Override
    public String getExchange() {
        return Constants.Exchanges.NEED_APPROVAL;
    }

    @Override
    public String getQueue() {
        return Constants.Queues.NEED_APPROVAL;
    }

//    @Override
//    public void run() {
//        try {
//            watchOpenOrders();
//        } catch (IOException | TimeoutException e) {
//            StaticLogger.logException(e);
//        }
//    }
}

