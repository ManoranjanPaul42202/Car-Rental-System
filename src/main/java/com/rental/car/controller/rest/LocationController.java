package com.rental.car.controller.rest;

import com.rental.car.entity.Location;
import com.rental.car.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // Get all locations
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    // Get location by ID
    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable long id) {
        return locationService.findById(id);
    }

    // Add a new location
    @PostMapping
    public Location addLocation(@RequestBody Location location) {
        return locationService.save(location);
    }

    // Update an existing location
    @PutMapping("/{id}")
    public Location updateLocation(@PathVariable long id, @RequestBody Location location) {
        location.setLocationId(id);
        return locationService.save(location);
    }

    // Delete a location by ID
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable long id) {
        locationService.deleteById(id);
    }

}
