/* package com.rental.car.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities());
    }
}
*/