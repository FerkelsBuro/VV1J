package th.vv3.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.StaleObjectStateException;
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
    @ApiOperation( // SWAGGER
            value = "Searches customer with ID",
            notes = "...",
            response = Customer.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 200, message = "Customer found"),
            @ApiResponse(code = 404, message = "Customer not found"),})
    public ResponseEntity getCustomerById(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation( // SWAGGER
            value = "Creates a Customer",
            response = Customer.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 201, message = "Customer created"),
            @ApiResponse(code = 400, message = "Bad Request"),})
    public ResponseEntity create(@RequestBody Customer customer) {
        if(customer.getCustomerId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        }

        try {
        customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            //TODO
            return new ResponseEntity<>("Email already used", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation( // SWAGGER
            value = "Change a Customer",
            response = Customer.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 201, message = "Customer changed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Customer not found"),})
    public ResponseEntity change(@RequestBody Customer customer) {
        if (customer.getCustomerId() == null) {
            return new ResponseEntity<>("id required", HttpStatus.BAD_REQUEST);
        }
        if (customerRepository.findById(customer.getCustomerId()).equals(Optional.empty())) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        try {
            customer = customerRepository.saveAndFlush(customer);
        }
        catch (Exception e) {
            return new ResponseEntity<>("version is outdated", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity delete(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            return new ResponseEntity<>("customer does not exist", HttpStatus.NOT_FOUND);
        }

        customerRepository.delete(customer.get());
        return new ResponseEntity<>("customer deleted", HttpStatus.OK);
    }
}
