package com.rental.car.service;

import com.rental.car.entity.Car;
import com.rental.car.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(int username);
    Customer save(Customer customer);
    void deleteById(int username);
}
