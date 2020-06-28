package th.vv3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import th.vv3.models.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
