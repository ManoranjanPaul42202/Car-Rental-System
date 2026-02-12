package com.rental.car.controller.rest;

import com.rental.car.entity.Customer;
import com.rental.car.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.save(customer);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    // Get customer by username
    @GetMapping("/{username}")
    public ResponseEntity<Customer> getCustomerByUsername(@PathVariable int username) {
        Optional<Customer> customer = customerService.findById(username);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete customer by username
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int username) {
        Optional<Customer> customer = customerService.findById(username);
        if (customer.isPresent()) {
            customerService.deleteById(username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
