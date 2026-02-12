package com.rental.car.repository;

import com.rental.car.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "customers")
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
