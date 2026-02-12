package com.rental.car.service;

import com.rental.car.entity.Customer;
import com.rental.car.entity.User;
import com.rental.car.repository.CustomerRepository;
import com.rental.car.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(int username) {
        return customerRepository.findById(username);
    }

    @Override
    public Customer save(Customer customer) {
        User user = customer.getUser();
        if (user != null && user.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            System.out.println(hashedPassword);
        }
        userRepository.save(user);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(int username) {
        customerRepository.deleteById(username);
    }
}
