package th.vv3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.repositories.CustomerRepository;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomersController {
    private CustomerRepository customerRepository;

    public CustomersController(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> Get() {
        return this.customerRepository.findAll();
    }

    @PostMapping
    public Customer Create(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer;
    }
}
