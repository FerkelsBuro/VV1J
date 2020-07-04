package th.vv3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.vv3.models.Payment;
import th.vv3.repositories.CustomerRepository;
import th.vv3.repositories.PaymentRepository;

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
            return  new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
        }

        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException e) {
            //TODO
            return new ResponseEntity<>("Id error", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }
}
