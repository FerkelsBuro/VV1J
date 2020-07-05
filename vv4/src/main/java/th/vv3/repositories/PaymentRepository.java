package th.vv3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import th.vv3.models.Customer;
import th.vv3.models.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    long countByCustomer(Customer customer);
}
