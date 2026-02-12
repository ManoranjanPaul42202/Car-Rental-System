package com.rental.car.service;

import com.rental.car.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> findAll();
    Optional<Location> findById(long locationId);
    Location save(Location location);
    void deleteById(long locationId);
}