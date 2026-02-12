package com.rental.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String customerId;

    @Column(name = "username")
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user; // Link to User table (One-to-One relationship)

    @Column(name = "first_name", nullable = false)
    private String firstName; // Customer's first name

    @Column(name = "last_name", nullable = false)
    private String lastName; // Customer's last name

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // Customer's phone number

    @Column(name = "email", unique = true, nullable = false)
    private String email; // Customer's email address

    @Column(name = "driver_license", nullable = false)
    private String driverLicense; // Customer's driver license
}
