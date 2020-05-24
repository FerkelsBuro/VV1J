package starter;

import com.google.gson.Gson;
import core.OrderApprovalStrategy;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import services.AccountingService;

public class Startup {
    private static final OrderApprovalStrategy strategy = new OrderApprovalStrategy();
    private final static Gson gson = new Gson();

    public static void main(String[] argv) throws Exception {
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        MessageSender messageSender = new MessageSender(gson);
        AccountingService accountingService = new AccountingService(messageReceiver, messageSender, strategy);

        accountingService.watchOpenOrders();
    }


}
