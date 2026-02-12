package com.rental.car.controller;

import com.rental.car.entity.*;
import com.rental.car.repository.BookingRepository;
import com.rental.car.repository.UserRepository;
import com.rental.car.service.BookingService;
import com.rental.car.service.CarService;
import com.rental.car.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CarService carService;

    @Autowired
    LocationService locationService;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @PostMapping("/dashboard/book/{carId}")
    public String addBooking(@PathVariable int carId,
                             @ModelAttribute("booking") Booking booking,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model,
                             RedirectAttributes redirectAttributes){
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser.get();
        booking.setUser(user);

        Optional<Car> optionalCar = carService.findById(carId);
        Car car = optionalCar.get();
        booking.setPickupLocation(car.getLocation());

        if (!car.isAvailabilityStatus()) {
            return "redirect:/dashboard"; // car unavailable
        }

        bookingService.save(booking);
        redirectAttributes.addFlashAttribute("successMessage", "Booking successfully added!");
        model.addAttribute("bookings", bookingRepository.findByUserUsername(user.getUsername(), Sort.by(Sort.Order.desc("bookingId"))));
        return "redirect:/dashboard/book/list";
    }

    @GetMapping("/dashboard/book/{carId}")
    public String getBooking(@PathVariable long carId, Model model) {
        Optional<Car> optionalCar = carService.findById(carId);
        if (optionalCar.isEmpty()) {
            return "redirect:/dashboard";
        }

        Car car = optionalCar.get();
        Booking booking = new Booking();

        // Initialize nested objects to avoid Thymeleaf errors
        booking.setCar(car);
        booking.setUser(new User()); // Assuming 'User' is the logged-in user
        booking.setPickupLocation(car.getLocation());
        booking.setDropoffLocation(new Location());
        booking.setBookingStatus(BookingStatus.PENDING); // Optional - already set in form
        model.addAttribute("car", car);
        model.addAttribute("booking", booking);
        model.addAttribute("locationList", locationService.findAll());
        model.addAttribute("similarCars", carService.findSimilarCars(car.getCarType(), carId));
        return "booking";
    }

    @GetMapping("/dashboard/book/list")
    public String getList(Model model,
                          @AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("Bookings",
                bookingRepository.findByUserUsername(userDetails.getUsername(), Sort.by(Sort.Order.desc("bookingId"))));
        return "booking-list";
    }

    @GetMapping("/dashboard/book/update/{bookingId}")
    public String updateBooking(@PathVariable long bookingId,
                                Model model){
        Optional<Booking> optionalBooking = bookingService.findById(bookingId);
        Booking booking = optionalBooking.get();
        Car car = booking.getCar();
        model.addAttribute("booking", booking);
        model.addAttribute("locationList", locationService.findAll());
        model.addAttribute("car", car);
        model.addAttribute("similarCars", carService.findSimilarCars(car.getCarType(), car.getCarId()));
        return "booking";
    }

    @GetMapping("/dashboard/book/delete/{bookingId}")
    public String deleteBooking(@PathVariable long bookingId,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails){
        Optional<Booking> optionalBooking = bookingService.findById(bookingId);
        Booking booking = optionalBooking.get();

        Car car = booking.getCar();
        car.setAvailabilityStatus(true);
        Car savedCar = carService.save(car);

        bookingService.deleteById(bookingId);

        redirectAttributes.addFlashAttribute("successMessage", "Booking Deleted Successfully");
        model.addAttribute("Bookings",
                bookingRepository.findByUserUsername(userDetails.getUsername(), Sort.by(Sort.Order.desc("bookingId"))));
        return "redirect:/dashboard/book/list";
    }

}
