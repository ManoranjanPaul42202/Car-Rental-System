package com.rental.car;

import com.rental.car.entity.Booking;
import com.rental.car.entity.BookingStatus;
import com.rental.car.entity.Notification;
import com.rental.car.repository.BookingRepository;
import com.rental.car.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingNotificationScheduler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookingRepository bookingRepository;

    // Runs every minute for debugging (change to "0 0 8 * * *" for daily at 8 AM)
    @Scheduled(cron = "0 * * * * *")
    public void sendDailyConfirmedBookingNotifications() {
        LocalDate today = LocalDate.now();
        List<Booking> todaysBookings = bookingRepository.findByStartDateAndBookingStatusAndNotifiedFalse(today, BookingStatus.CONFIRMED);

        for (Booking booking : todaysBookings) {
            Notification notification = new Notification();
            notification.setUser(booking.getUser()); // assuming Booking has getUser()
            notification.setBooking(booking);
            notification.setMessage("Your car rental for " + booking.getCar().getName() + " is confirmed today.");
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSeen(false);
            booking.setNotified(true);

            bookingRepository.save(booking);
            notificationService.save(notification);
        }
    }
}
