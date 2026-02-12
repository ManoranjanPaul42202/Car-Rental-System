package com.rental.car.service;

import com.rental.car.entity.Car;
import com.rental.car.repository.CarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarServiceImpl implements CarService{

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> findById(long carId) {
        return carRepository.findById(carId);
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteById(long carId) {
        carRepository.deleteById(carId);
    }

    @Override
    public List<Car> findByName(String name) {
        return carRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Car> findByState(String state) {
        //return carRepository.findByStateContainingIgnoreCase(state);
        TypedQuery<Car> theQuery = entityManager.createQuery("select c from Car c where c.location.state = :state", Car.class);
        theQuery.setParameter("state", state);
        return theQuery.getResultList();
    }

    @Override
    public List<Car> findByCity(String city) {
        //return carRepository.findByCityContainingIgnoreCase(city);
        TypedQuery<Car> theQuery = entityManager.createQuery("select c from Car c where c.location.city = :city",
                Car.class);
        theQuery.setParameter("city", city);
        return theQuery.getResultList();
    }

    @Override
    public List<Car> findByStateCity(String state, String city) {
        TypedQuery<Car> theQuery = entityManager.createQuery("select c from Car c where c.location.city = :city " +
                "AND c.location.state = :state", Car.class);
        theQuery.setParameter("city", city);
        theQuery.setParameter("state", state);
        return theQuery.getResultList();
    }

    @Override
    public List<Car> findBySeating() {
        return carRepository.findAllByOrderBySeatingCapacityDesc();
    }

    @Override
    public List<Car> findByRentalPriceAsc() {
        return carRepository.findAllByOrderByRentalPricePerDayAsc();
    }

    @Override
    public List<Car> findByRentalPriceDesc() {
        return carRepository.findAllByOrderByRentalPricePerDayDesc();
    }

    @Override
    public List<Car> findSimilarCars(String carType, Long carId) {
        return carRepository.findByCarTypeAndCarIdNot(carType, carId);
    }
}
