package com.rental.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    public Long carId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "car_type", nullable = false)
    private String carType; // e.g., "SUV", "Sedan", "Sports"


    @Column(name = "model_number", nullable = false)
    private String modelNumber;

    @Column(name = "year_of_production", nullable = false)
    private int yearOfProduction;

    @Column(name = "registration_number", unique = true, nullable = false)
    private String registrationNumber;

    @Column(name = "seating_capacity", nullable = false)
    private int seatingCapacity;

    @Column(name = "rental_price_per_day", nullable = false)
    private int rentalPricePerDay;

    @Column(name = "availability_status", nullable = false)
    private boolean availabilityStatus;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "engine_type", nullable = false)
    private String engineType;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private Location location;
}
