package ch.pitaya.pitaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.V_FileSummary;

public interface V_FileSummaryRepository extends JpaRepository<V_FileSummary, Long> {

	List<V_FileSummary> findByCaseIdAndUserId(Long caseId, Long userId);

}
