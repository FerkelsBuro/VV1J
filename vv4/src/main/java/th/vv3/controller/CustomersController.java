package th.vv3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.repositories.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
public class CustomersController {
    private CustomerRepository customerRepository;

    public CustomersController(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(this.customerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("{customerId}")
    public ResponseEntity getCustomerById(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Customer customer) {
        if(customer.getCustomerId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        }

        try {
        customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email already used", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity change(@RequestBody Customer customer) {
        if (customer.getCustomerId() == null) {
            return new ResponseEntity<>("id required", HttpStatus.BAD_REQUEST);
        }
        if (customerRepository.findById(customer.getCustomerId()).equals(Optional.empty())) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        customerRepository.save(customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
