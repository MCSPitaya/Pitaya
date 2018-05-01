package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;

public interface CaseRepository extends JpaRepository<Case, Long> {

	Optional<Case> findByIdAndFirm(Long id, Firm firm);

}
