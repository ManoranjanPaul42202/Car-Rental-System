package com.rental.car.service;

import com.rental.car.entity.Booking;
import com.rental.car.entity.Notification;
import com.rental.car.entity.User;

import java.util.List;

public interface NotificationService {

    List<Notification> findByUserAndUnseen(User user);
    Notification save(Notification notification);
    List<Notification> findByUserCreatedAtDesc(User user);
    List<Notification> markAllAsReadByUser(User user);
}
