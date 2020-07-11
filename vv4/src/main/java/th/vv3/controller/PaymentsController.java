package th.vv3.controller;

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


    // LW: customers/{customerId}/payments passt hier vielleicht besser
    @PostMapping
    public ResponseEntity create(@RequestBody Payment payment) {
        // LW: DTO ohne Id wäre eventuell praktisch
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
        // LW: ggf. order.validatePayment(payment) Methode einführen
        if (order.get().getAmount() < payment.getAmount()) {
            return new ResponseEntity<>("Order.amount < Payment.amount is not allowed", HttpStatus.BAD_REQUEST);
        }

        paymentRepository.save(payment);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("{customerId}/count")
    public ResponseEntity getAmountByCustomerId(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        // LW: Lässt sich über SQL selbst bestimmt besser lösen ist aber auf die schnelle wahrscheinlich zu kompliziert
        // LW: Man könnte die 7 Zeilen aber in eine extra/andere Klasse auslagern
        int amountOrders = orderRepository.findAll()
                .stream()
                .filter(e -> e.getCustomer().getCustomerId().equals(customerId)).mapToInt(Order::getAmount).sum();

        int amountPayed = paymentRepository.findAll()
                .stream()
                .filter(e -> e.getCustomerId().equals(customerId)).mapToInt(Payment::getAmount).sum();

        int amountOpen = amountOrders - amountPayed;

        return new ResponseEntity<>(amountOpen, HttpStatus.OK);
    }
}
