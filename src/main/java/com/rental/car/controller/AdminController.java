package com.rental.car.controller;

import com.rental.car.entity.Booking;
import com.rental.car.entity.BookingStatus;
import com.rental.car.entity.Car;
import com.rental.car.service.BookingService;
import com.rental.car.service.CarService;
import com.rental.car.service.EmailService;
import com.rental.car.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    BookingService bookingService;

    @Autowired
    CarService carService;

    @Autowired
    LocationService locationService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/admin/dashboard")
    public String getAdminPanel(Model model,
                                @AuthenticationPrincipal UserDetails userDetails){
        List<Booking> listBooking = bookingService.findAll();
        listBooking.forEach(booking -> {
            System.out.println("Booking Status: " + booking.getBookingStatus());
        });
        model.addAttribute("Bookings", listBooking);

        return "admin-panel";
    }

    @GetMapping("/admin/dashboard/book/update/{bookingId}")
    public String approveRequest(Model model,
                                 @PathVariable long bookingId){
        Optional<Booking> optionalBooking = bookingService.findById(bookingId);
        Booking booking = optionalBooking.get();
        Car car = booking.getCar();
        if(car.isAvailabilityStatus()){
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            Booking savedBooking = bookingService.save(booking);
            car.setAvailabilityStatus(false);
            Car savedCar = carService.save(car);
            // 📧 Send email
            emailService.sendBookingConfirmationEmail(booking);
            return "redirect:/admin/dashboard";
        }
        else return "admin-panel";
    }

    @GetMapping("/admin/dashboard/book/delete/{bookingId}")
    public String rejectBooking(Model model,
                                @PathVariable long bookingId){
        Optional<Booking> optionalBooking = bookingService.findById(bookingId);
        Booking booking = optionalBooking.get();
        Car car = booking.getCar();
        booking.setBookingStatus(BookingStatus.REJECTED);
        Booking savedBooking = bookingService.save(booking);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard/cars")
    public String carData(Model model){
        model.addAttribute("car", new Car());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("carList", carService.findAll());
        return "add-car";
    }

    @PostMapping("/admin/dashboard/cars/save")
    public String saveCar(@ModelAttribute Car car) {
            // Save car to DB
            carService.save(car);
            return "redirect:/admin/dashboard"; // Redirect after success
    }

    @GetMapping("/admin/dashboard/available/{carId}")
    public String setAvailability(Model model,
                                  @PathVariable long carId){
        Optional<Car> optionalCar = carService.findById(carId);
        Car car = optionalCar.get();
        car.setAvailabilityStatus(true);
        Optional<Booking> optionalBooking = bookingService.findLatest(carId);
        Booking booking = optionalBooking.get();
        car.setLocation(booking.getDropoffLocation());
        Car savedCar = carService.save(car);
        return "redirect:/admin/dashboard/cars";
    }

}

