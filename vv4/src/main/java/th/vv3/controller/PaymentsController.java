package th.vv3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.models.Payment;
import th.vv3.repositories.CustomerRepository;
import th.vv3.repositories.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentsController {
    private PaymentRepository paymentRepository;
    private CustomerRepository customerRepository;

    PaymentsController(@Autowired PaymentRepository paymentRepository, @Autowired CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Payment payment) {
        if (payment.getPaymentId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        }
        if (customerRepository.findById(payment.getCustomerId()).isEmpty()) {
            return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
        }

        paymentRepository.save(payment);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("{customerId}")
    public ResponseEntity getCustomerById(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        int amount = paymentRepository.findAll()
                .stream()
                .filter(e -> e.getCustomerId().equals(customerId)).mapToInt(Payment::getAmount).sum();

        return new ResponseEntity<>(amount, HttpStatus.OK);
    }
}
