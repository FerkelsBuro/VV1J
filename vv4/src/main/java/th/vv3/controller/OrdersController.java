package th.vv3.controller;

import jdk.internal.joptsimple.internal.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.vv3.models.Order;
import th.vv3.repositories.OrderRepository;

@RestController
@RequestMapping("api/v1/orders")
public class OrdersController {
    private OrderRepository orderRepository;

    public OrdersController(@Autowired OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Order order) {
        if (order.getOrderId() != null) {
            return new ResponseEntity<>("id must not be set", HttpStatus.BAD_REQUEST);
        } else if (order.getApprovedBy().equals(Strings.EMPTY)) {
            return new ResponseEntity<>("Order must be approved by someone", HttpStatus.BAD_REQUEST);
        } else if (order.getCustomer() == null) {
            return new ResponseEntity<>("Order must have a customer", HttpStatus.BAD_REQUEST);
        }

        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            //TODO
            return new ResponseEntity<>("Customer does not exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
