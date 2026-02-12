package com.rental.car.repository;

import com.rental.car.entity.Notification;
import com.rental.car.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndSeenFalse(User user);
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
