package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
