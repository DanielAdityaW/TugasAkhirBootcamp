    package com.bcafbootcamp.tugasakhirbootcamp.controller;

    import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
    import com.bcafbootcamp.tugasakhirbootcamp.service.CustomerService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/customers")
    @CrossOrigin(origins = "http://localhost:4200")
    public class CustomerListController {

        @Autowired
        private CustomerService customerService;

        @GetMapping
        public Page<Customer> getCustomers(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "100") int size,
                @RequestParam(defaultValue = "desc") String sortOrder,
                @RequestParam(required = true) String vehicleType,
                @RequestParam(required = false) String customerId // Add search by customerId
        ) {
            return customerService.getSortedCustomerData(page, size, sortOrder, vehicleType, customerId);
        }

    }

