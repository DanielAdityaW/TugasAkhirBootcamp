package com.bcafbootcamp.tugasakhirbootcamp.repo;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

    List<Customer> findByIsNunggak(boolean val);

    Optional<Customer> findById(String CustomerId);

    Page<Customer> findByVehicleType(String vehicleType, Pageable pageable);

    // Add method to search with wildcard using LIKE
    Page<Customer> findByCustomerIdContaining(String customerId, Pageable pageable); // Use LIKE with wildcard
}
