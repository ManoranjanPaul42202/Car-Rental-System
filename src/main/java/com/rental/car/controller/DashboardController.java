package com.rental.car.controller;

import com.rental.car.entity.*;
import com.rental.car.repository.BookingRepository;
import com.rental.car.repository.NotificationRepository;
import com.rental.car.repository.UserRepository;
import com.rental.car.service.CarService;
import com.rental.car.service.NotificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    CarService carService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    BookingRepository bookingRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @GetMapping("/dashboard")
    public String findAll(Model model,
                          @AuthenticationPrincipal UserDetails userDetails,
                          Principal principal){
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Car> carList = carService.findAll();
        model.addAttribute("carList", carList);

        // Get logged-in username
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName(); // returns principal (username)
//        model.addAttribute("user", username);

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        User user = optionalUser.get();
        List<Notification> notificationList =  notificationRepository.findByUserAndSeenFalse(user);
        List<Notification> allNotification = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        model.addAttribute("notification", allNotification);
        model.addAttribute("unseenCount", notificationList.stream().count());
        model.addAttribute("isAdmin", isAdmin);
        return "dashboard";
    }

    @GetMapping("/dashboard/search")
    public String findByName(@RequestParam String name, Model model){
        List<Car> carList =  carService.findByName(name);
        model.addAttribute("carList", carList);
        return "dashboard";
    }

    @GetMapping("/dashboard/sort")
    public String findCars(@RequestParam(value = "sortBy", required = false) String sortBy,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam(value = "city", required = false) String city,
                           Model model) {

        List<Car> carList;

        if ("priceAsc".equals(sortBy)) {
            carList = carService.findByRentalPriceAsc();
        } else if ("priceDesc".equals(sortBy)) {
            carList = carService.findByRentalPriceDesc();
        } else if ("seats".equals(sortBy)) {
            carList = carService.findBySeating();
        } else if (state != null && !state.isEmpty() && city != null && !city.isEmpty()) {
            // First try with both
            carList = carService.findByStateCity(state, city);

            // Optional: fallback if no results
            if (carList.isEmpty()) {
                carList = carService.findByCity(state); // or city-only fallback
            }
        } else if (state != null && !state.isEmpty()) {
            carList = carService.findByState(state);
        } else if (city != null && !city.isEmpty()) {
            carList = carService.findByCity(city);
        } else {
            carList = carService.findAll(); // Default: no filters
        }

        model.addAttribute("carList", carList);
        return "dashboard";
    }


    @GetMapping("/dashboard/cars/{carId}")
    public String getCarById(@PathVariable int carId, Model model) {
        Optional<Car> carOptional = carService.findById(carId);

        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            List<Car> similarCars = carService.findSimilarCars(car.getCarType(), car.getCarId());
            model.addAttribute("car", car);
            model.addAttribute("similarCars", similarCars);
            return "car-details";
        } else {
            return "redirect:/dashboard"; // or show a 404 page
        }
    }

    @GetMapping("/register")
    public String getUser(Model model){
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/register")
    @Transactional
    public String createUser(Model model,
                             @ModelAttribute("user") User user){
        user.setEnabled(1);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        // Insert into authorities
        Authority authority = new Authority();
        authority.setUsername(savedUser.getUsername()); // or getEmail() depending on your User model
        authority.setRole("ROLE_CUSTOMER");

        // Optional check if already exists
        TypedQuery<Authority> query = entityManager.createQuery(
                "SELECT a FROM Authority a WHERE a.username = :username AND a.role = :role", Authority.class);
        query.setParameter("username", authority.getUsername());
        query.setParameter("role", "ROLE_CUSTOMER");

        if (query.getResultList().isEmpty()) {
            entityManager.persist(authority);
        }
        return "redirect:/login";
    }

    @GetMapping("/notifications/read")
    public String markAllAsRead(Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName()); // Get logged-in user
        User user = optionalUser.get();
        notificationService.markAllAsReadByUser(user);
        return "redirect:/dashboard"; // Or redirect back to the current page
    }

    @GetMapping("/terms")
    public String getTerms(){
        return "terms";
    }

    @GetMapping("/booking/confirm/{id}")
    public String confirmRental(@PathVariable long id){
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        Booking booking = optionalBooking.get();
        booking.setRental(true);
        bookingRepository.save(booking);
        return "confirm-page";
    }
}
