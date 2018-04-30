package ch.pitaya.pitaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;

public interface CaseRepository extends JpaRepository<Case, Long> {

	List<Case> findByFirmId(Long firmId);
	
	List<Case> findByFirm(Firm firm);

}
