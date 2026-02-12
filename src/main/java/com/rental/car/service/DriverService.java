package com.rental.car.service;

import com.rental.car.entity.Customer;
import com.rental.car.entity.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<Driver> findAll();
    Optional<Driver> findById(int driverId);
    Driver save(Driver driver);
    void deleteById(int driverId);
}
