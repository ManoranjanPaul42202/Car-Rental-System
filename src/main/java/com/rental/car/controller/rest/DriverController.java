package com.rental.car.controller.rest;
import com.rental.car.entity.Driver;
import com.rental.car.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {
        try {
            Driver createdDriver = driverService.save(driver);
            return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get all customers
    @GetMapping
    public List<Driver> getAllDriver() {
        return driverService.findAll();
    }

    // Get customer by username
    @GetMapping("/{username}")
    public ResponseEntity<Driver> getDriverByUsername(@PathVariable int username) {
        Optional<Driver> driver = driverService.findById(username);
        return driver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete customer by username
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteDriver(@PathVariable int username) {
        Optional<Driver> driver = driverService.findById(username);
        if (driver.isPresent()) {
            driverService.deleteById(username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
