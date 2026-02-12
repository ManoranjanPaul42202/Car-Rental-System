package com.rental.car.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class CustomerSecurityConfig {

    @Bean
    public SecurityFilterChain customersSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> request.getRequestURI().startsWith("/api/customers"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/customers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customers/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
