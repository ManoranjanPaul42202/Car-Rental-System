package com.rental.car.service;

import com.rental.car.entity.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> findAll();
    Optional<Car> findById(long carId);
    Car save(Car car);
    void deleteById(long carId);
    List<Car> findByName(String name);
    List<Car> findByState(String state);
    List<Car> findByCity(String city);
    List<Car> findByStateCity(String state, String city);
    List<Car> findBySeating();
    List<Car> findByRentalPriceAsc();
    List<Car> findByRentalPriceDesc();
    List<Car> findSimilarCars(String carType, Long carId);
}
