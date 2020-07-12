package th.vv3.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.models.Order;
import th.vv3.models.Payment;
import th.vv3.repositories.CustomerRepository;
import th.vv3.repositories.OrderRepository;
import th.vv3.repositories.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

@Api(tags = "Payment", description = "Create Payments and get open amount of specific Customers")
@RestController
@RequestMapping("api/v1/payments")
public class PaymentsController {
    private PaymentRepository paymentRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    PaymentsController(@Autowired PaymentRepository paymentRepository, @Autowired CustomerRepository customerRepository,
                       @Autowired OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    @ApiOperation( // SWAGGER
            value = "Creates a payment",
            notes = "...",
            response = Payment.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 201, message = "Payment created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Customer not found or Order not found"),})
    public ResponseEntity create(@RequestBody Payment payment) {
        if (payment.getPaymentId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        }
        if (customerRepository.findById(payment.getCustomerId()).isEmpty()) {
            return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
        }

        Optional<Order> order = orderRepository.findById(payment.getOrderId());
        if (order.isEmpty()) {
            return new ResponseEntity<>("Order does not exist", HttpStatus.NOT_FOUND);
        }
        if (order.get().getAmount() < payment.getAmount()) {
            return new ResponseEntity<>("Order.amount < Payment.amount is not allowed", HttpStatus.BAD_REQUEST);
        }

        paymentRepository.save(payment);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("{customerId}/count")
    @ApiOperation( // SWAGGER
            value = "Get open amount of a customer",
            notes = "...",
            response = Integer.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 200, message = "Returned open amount"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Customer not found"),})
    public ResponseEntity getAmountByCustomerId(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        int amountOpen = getAmountOpen(customerId);

        return new ResponseEntity<>(amountOpen, HttpStatus.OK);
    }

    private int getAmountOpen(UUID customerId) {
        int amountOrders = getAmountOrders(customerId);

        int amountPayed = getAmountPayed(customerId);

        return amountOrders - amountPayed;
    }

    private int getAmountPayed(UUID customerId) {
        return paymentRepository.findAll()
                .stream()
                .filter(e -> e.getCustomerId().equals(customerId)).mapToInt(Payment::getAmount).sum();
    }

    private int getAmountOrders(UUID customerId) {
        return orderRepository.findAll()
                .stream()
                .filter(e -> e.getCustomer().getCustomerId().equals(customerId)).mapToInt(Order::getAmount).sum();
    }
}
