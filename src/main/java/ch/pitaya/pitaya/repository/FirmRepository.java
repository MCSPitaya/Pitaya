package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Firm;

public interface FirmRepository extends JpaRepository<Firm, Long> {

}
