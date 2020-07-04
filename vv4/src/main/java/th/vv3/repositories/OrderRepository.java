package th.vv3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import th.vv3.models.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
