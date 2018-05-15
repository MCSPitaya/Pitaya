package ch.pitaya.pitaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.V_CaseNotification;

public interface V_CaseNotificationRepository extends JpaRepository<V_CaseNotification, Long> {

	List<V_CaseNotification> findByCaseIdOrFileCaseId(Long caseId, Long fileCaseId);

}
