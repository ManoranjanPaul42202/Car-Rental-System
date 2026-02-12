package com.rental.car.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class CarSecurityConfig {

    /*@Bean
    UserDetailsManager userDetailsManager(DataSource dataSource){

        JdbcUserDetailsManager user = new JdbcUserDetailsManager(dataSource);
        user.setUsersByUsernameQuery("select username, password, enabled from user where username=?");

        user.setAuthoritiesByUsernameQuery("select username, role from authorities where username=?");

        return user;
    }*/

    @Bean
    @Order(1)
    public SecurityFilterChain carsSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/cars/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/cars").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cars").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/cars/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_DRIVER", "ROLE_MANAGER", "ROLE_ADMIN")
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
