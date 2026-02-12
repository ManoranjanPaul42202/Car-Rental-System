package com.rental.car.repository;

import com.rental.car.entity.Booking;
import com.rental.car.entity.BookingStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUsernameAndBookingIdNot(String username, Long bookingId);

    List<Booking> findByUserUsername(String username, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.car.carId = :carId AND b.bookingId = (SELECT MAX(b2.bookingId) " +
            "FROM Booking b2 WHERE b2.car.carId = :carId)")
    Optional<Booking> findLatestBooking(long carId);

    List<Booking> findByStartDateAndBookingStatusAndNotifiedFalse(LocalDate start, BookingStatus bookingStatus);
}
