package com.rental.car.controller;

import com.rental.car.entity.Car;
import com.rental.car.entity.User;
import com.rental.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Scanner;

@Controller
public class LoginController {

    @Autowired
    CarService carService;

//    @PostMapping("/login")
//    public String getLogin(Model model, User user){
//        model.addAttribute("user", user.getUsername());
//        return "login";
//    }

    @GetMapping("/login")
    public String showLogin(Model model, User user){
        model.addAttribute("user", user.getUsername());
        return "login";
    }

}
