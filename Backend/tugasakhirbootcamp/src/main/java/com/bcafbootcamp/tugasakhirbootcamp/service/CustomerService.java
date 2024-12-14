package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public List<Customer> getAllCustomerData() {
        return customerRepo.findByIsNunggak(false);
    }

    public Page<Customer> getSortedCustomerData(int page, int size, String sortOrder, String vehicleType, String customerId) {
        Sort sort = Sort.by("riskScore");
        if ("desc".equalsIgnoreCase(sortOrder)){
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        // Check if customerId is provided and search accordingly with wildcard
        if (customerId != null && !customerId.isEmpty()) {
            return customerRepo.findByCustomerIdContaining(customerId, pageable); // Use LIKE wildcard
        }

        return customerRepo.findByVehicleType(vehicleType, pageable);
    }
}
