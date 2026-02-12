package com.rental.car.repository;

import com.rental.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByNameContainingIgnoreCase(String name);
    //List<Car> findByStateContainingIgnoreCase(String state);
    //List<Car> findByCityContainingIgnoreCase(String city);
    List<Car> findAllByOrderByRentalPricePerDayAsc();
    List<Car> findAllByOrderByRentalPricePerDayDesc();
    List<Car> findAllByOrderBySeatingCapacityDesc();
    List<Car> findByCarTypeAndCarIdNot(String carType, Long carId);


}
