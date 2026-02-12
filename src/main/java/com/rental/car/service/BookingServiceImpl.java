package com.rental.car.service;

import com.rental.car.entity.Booking;
import com.rental.car.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> findById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public void deleteById(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public Optional<Booking> findLatest(long carId) {
        return bookingRepository.findLatestBooking(carId);
    }
}
