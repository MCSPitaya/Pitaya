package ch.pitaya.pitaya.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.V_CaseSummary;

public interface V_CaseSummaryRepository extends JpaRepository<V_CaseSummary, Long> {

	Stream<V_CaseSummary> findAllByUserId(Long userId);

	Optional<V_CaseSummary> findByIdAndUserId(Long caseId, Long userId);

}
