package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "v_case_notification")
public class V_CaseNotification {

	@Id
	private Long id;

	private Timestamp cre_dat;

	private Long userId, firmId, caseId, fileId, fileCaseId;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private String notes;

	protected V_CaseNotification() {
		// JPA
	}

	public Long getId() {
		return id;
	}

	public Timestamp getCre_dat() {
		return cre_dat;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getFirmId() {
		return firmId;
	}

	public Long getCaseId() {
		return caseId;
	}

	public Long getFileId() {
		return fileId;
	}

	public Long getFileCaseId() {
		return fileCaseId;
	}

	public NotificationType getType() {
		return type;
	}

	public String getNotes() {
		return notes;
	}

}
