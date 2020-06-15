package core;

/**
 * Class where public constants are stored
 */
public class Constants {
    private Constants() {
    }

    public static class Queues {
        public static final String OPEN_ORDERS = "OpenOrdersQueue";
        public static final String NEED_APPROVAL = "NeedsApprovalQueue";
        public static final String APPROVED_ORDERS = "ApprovedOrdersQueue";
        public static final String DECLINED_ORDER = "DeclinedOrdersQueue";
        private Queues() {
        }
    }

    public static class Exchanges {
        public static final String OPEN_ORDERS = "OpenOrdersExchange";
        public static final String NEED_APPROVAL = "NeedsApprovalExchange";
        public static final String APPROVED_ORDERS = "ApprovedOrdersExchange";
        public static final String DECLINED_ORDER = "DeclinedOrdersExchange";
        private Exchanges() {
        }
    }
}
