package com.rental.car.controller.rest;

import com.rental.car.entity.Car;
import com.rental.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping()
    public List<Car> findAll(){
        return carService.findAll();
    }

    @GetMapping("/{carId}")
    public Optional<Car> findById(@PathVariable Long carId){
        return carService.findById(carId);
    }

    @GetMapping("/search")
    public List<Car> findByName(@RequestParam String name){
        return carService.findByName(name);
    }

    @GetMapping("/location")
    public ResponseEntity<?> findByState(@RequestParam String state){
        List<Car> carList = carService.findByState(state);
        if(carList.isEmpty())
            return ResponseEntity.ok("No Car Found At the provided State");
        else
            return ResponseEntity.ok(carList);
    }

    @GetMapping("/location/city")
    public ResponseEntity<?> findByCity(@RequestParam String city) {
        List<Car> carList = carService.findByCity(city);
        if (carList.isEmpty()) {
            return ResponseEntity.ok("No Car Found At the provided city");
        } else {
            return ResponseEntity.ok(carList);
        }
    }

    @PostMapping()
    public Car addCar(@RequestBody Car car){
        return carService.save(car);
    }

    @PutMapping()
    public Car updateCar(@RequestBody Car car){
        return carService.save(car);
    }

    @DeleteMapping("/{carId}")
    public String deleteCar(@PathVariable long carId){
        Optional<Car> tempCar = carService.findById(carId);
        if(tempCar.isEmpty())
            throw new RuntimeException("Car with Id :" + carId + "not available");
        else
            carService.deleteById(carId);
        return "Car Deleted Successfully";
    }
}
