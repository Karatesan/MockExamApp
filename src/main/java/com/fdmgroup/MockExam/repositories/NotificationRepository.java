package com.fdmgroup.MockExam.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	List<Notification> findByHandleBy(User admin);

	List<Notification> findByUser(User user);

}
