package com.rental.car.service;

import com.rental.car.entity.Driver;
import com.rental.car.repository.DriverRepository;
import com.rental.car.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    public Optional<Driver> findById(int username) {
        return driverRepository.findById(username);
    }

    @Override
    public Driver save(Driver driver) {
        userRepository.save(driver.getUser());
        return driverRepository.save(driver);
    }

    @Override
    public void deleteById(int username) {
        driverRepository.deleteById(username);
    }
}
