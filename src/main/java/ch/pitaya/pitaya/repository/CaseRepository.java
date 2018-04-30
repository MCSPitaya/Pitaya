package ch.pitaya.pitaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Case;

public interface CaseRepository extends JpaRepository<Case, Long> {

	List<Case> findByFirmId(Long firmId);

}
