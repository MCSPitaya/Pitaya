package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {

}
