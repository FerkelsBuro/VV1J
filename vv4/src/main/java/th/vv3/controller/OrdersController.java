package th.vv3.controller;

// LW: Das soll denke ich nicht importiert weren ('internal')
import jdk.internal.joptsimple.internal.Strings;
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
    public ResponseEntity create(@RequestBody Order order, @PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            return new ResponseEntity<>("customer not found", HttpStatus.NOT_FOUND);
        } 
        // LW: Besser wäre es sowas über seperate Validatioren abzufangen
        // LW: Vielleicht ist das hier was: https://hibernate.org/validator/
        // LW: Geht zumindest in die Richtung
        else if (order.getOrderId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        } else if (order.getApprovedBy().equals(Strings.EMPTY)) {
            return new ResponseEntity<>("Order must be approved by someone", HttpStatus.BAD_REQUEST);
        }

        order.setCustomer(customer.get());

        orderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
