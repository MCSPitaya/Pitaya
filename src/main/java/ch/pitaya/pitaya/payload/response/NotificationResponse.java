package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.Notification;
import ch.pitaya.pitaya.model.User;

import static ch.pitaya.pitaya.util.Utils.ifNotNull;

public class NotificationResponse {

	private Long id, userId, firmId, caseId, fileId;
	private String type, notes;
	private Timestamp time;

	public NotificationResponse(Notification notification) {
		id = notification.getId();
		userId = ifNotNull(notification.getUser(), User::getId);
		firmId = ifNotNull(notification.getFirm(), Firm::getId);
		caseId = ifNotNull(notification.getCase(), Case::getId);
		fileId = ifNotNull(notification.getFile(), File::getId);
		type = notification.getType().toString();
		notes = notification.getNotes();
		time = notification.getCreationTime();
	}

	public Long getId() {
		return id;
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

	public String getType() {
		return type;
	}

	public String getNotes() {
		return notes;
	}

	public Timestamp getTime() {
		return time;
	}

}
