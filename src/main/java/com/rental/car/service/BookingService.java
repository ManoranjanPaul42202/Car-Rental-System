package com.rental.car.service;

import com.rental.car.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking save(Booking booking);
    List<Booking> findAll();
    Optional<Booking> findById(Long bookingId);
    void deleteById(Long bookingId);
    Optional<Booking> findLatest(long carId);

}
