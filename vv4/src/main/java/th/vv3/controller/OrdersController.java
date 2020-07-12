package th.vv3.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.models.Customer;
import th.vv3.models.Order;
import th.vv3.repositories.CustomerRepository;
import th.vv3.repositories.OrderRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers/{customerId}/orders")
public class OrdersController {
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;

    public OrdersController(@Autowired OrderRepository orderRepository, @Autowired CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    @ApiOperation( // SWAGGER
            value = "Creates an order",
            notes = "...",
            response = Order.class,
            produces = "application/json")
    @ApiResponses(value = { // SWAGGER
            @ApiResponse(code = 200, message = "Order created"),
            @ApiResponse(code = 400, message = "Bad Request"),})
    public ResponseEntity create(@RequestBody Order order, @PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            return new ResponseEntity<>("customer not found", HttpStatus.NOT_FOUND);
        } else if (order.getOrderId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        } else if (order.getApprovedBy().equals(Strings.EMPTY)) {
            return new ResponseEntity<>("Order must be approved by someone", HttpStatus.BAD_REQUEST);
        }

        order.setCustomer(customer.get());

        orderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
