package com.rental.car.controller;

import com.rental.car.entity.Notification;
import com.rental.car.entity.User;
import com.rental.car.repository.NotificationRepository;
import com.rental.car.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class NotificationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String getNotification(Model model, Principal principal){
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        User user = optionalUser.get();
        List<Notification> notificationList =  notificationRepository.findByUserAndSeenFalse(user);
        model.addAttribute("notification", notificationList);
        return "dashboard";
    }
}
