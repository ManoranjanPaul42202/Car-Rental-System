package com.rental.car.service;

import com.rental.car.entity.Booking;
import com.rental.car.entity.Notification;
import com.rental.car.entity.User;
import com.rental.car.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> findByUserAndUnseen(User user) {
        return notificationRepository.findByUserAndSeenFalse(user);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findByUserCreatedAtDesc(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Notification> markAllAsReadByUser(User user) {
        List<Notification> notificationList = notificationRepository.findByUserAndSeenFalse(user);
        for(Notification notification : notificationList){
            notification.setSeen(true);
        }
        return notificationRepository.saveAll(notificationList);
    }
}
