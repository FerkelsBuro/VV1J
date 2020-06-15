package starter;

import com.google.gson.Gson;
import core.OrderApprovalStrategy;
import infrastructure.MessageReceiver;
import infrastructure.MessageSender;
import services.TeamLeadService;

public class Startup {
    private static final Gson gson = new Gson();

    public static void main(String[] argv) throws Exception {
        MessageReceiver messageReceiver = new MessageReceiver(gson);
        MessageSender messageSender = new MessageSender(gson);

        OrderApprovalStrategy strategy = new OrderApprovalStrategy();

        TeamLeadService teamLeadService = new TeamLeadService(messageReceiver, messageSender, strategy);
        teamLeadService.watchOpenOrders();
    }
}
