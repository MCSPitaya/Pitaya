package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.V_CaseAuth;

public interface V_CaseAuthRepository extends JpaRepository<V_CaseAuth, Long> {

	Optional<V_CaseAuth> findByIdAndUserId(Long caseId, Long userId);

}
