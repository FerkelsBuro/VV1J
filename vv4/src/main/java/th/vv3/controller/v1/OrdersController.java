package th.vv3.controller.v1;

import core.loggers.StaticLogger;
import io.swagger.annotations.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.vv3.DTOs.OrderReadDto;
import th.vv3.models.Customer;
import th.vv3.models.Order;
import th.vv3.repositories.CustomerRepository;
import th.vv3.repositories.OrderRepository;

import java.util.Optional;
import java.util.UUID;

@Api(tags = "Order", description = "Create Orders")
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
    public ResponseEntity create(@ApiParam(value = "Order that should be created", required = true) @RequestBody OrderReadDto orderRead,
                                 @ApiParam(value = "Unique Id of the Order's Customer", required = true) @PathVariable UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            return new ResponseEntity<>("customer not found", HttpStatus.NOT_FOUND);
        } else if (orderRead.getApprovedBy().equals(Strings.EMPTY)) {
            return new ResponseEntity<>("Order must be approved by someone", HttpStatus.BAD_REQUEST);
        }

        if (orderRead.getOrderId() != null && orderRepository.findById(orderRead.getOrderId()).isPresent()) {
            return new ResponseEntity<>("Order with same Id already exists", HttpStatus.CONFLICT);
        }
        Order order = new Order(orderRead);
        order.setCustomer(customer.get());

        orderRepository.saveAndFlush(order);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
