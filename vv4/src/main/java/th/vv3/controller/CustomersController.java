package th.vv3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customers")
public class CustomersController {
    private CustomerRepository customerRepository;

    public CustomersController(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> get() {
        return this.customerRepository.findAll();
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer;
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
