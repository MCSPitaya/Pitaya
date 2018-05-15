package ch.pitaya.pitaya.payload.response;

import static ch.pitaya.pitaya.util.Utils.ifNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.Notification;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.model.V_CaseNotification;

public class NotificationResponse {

	public static class FileNotificationResponse extends NotificationResponse {
		private List<Long> caseIds = new ArrayList<Long>();

		private FileNotificationResponse(Notification notification) {
			super(notification);
			for (Case case1 : notification.getFile().getCases()) {
				caseIds.add(case1.getId());
			}
		}

		public List<Long> getCaseIds() {
			return caseIds;
		}
	}

	public static class CaseNotificationResponse extends NotificationResponse {

		private Long caseId;

		private CaseNotificationResponse(Notification notification) {
			super(notification);
			caseId = ifNotNull(notification.getCase(), Case::getId);
		}

		public CaseNotificationResponse(V_CaseNotification notification) {
			super(notification);
			if (notification.getCaseId() == null)
				caseId = notification.getFileCaseId();
			else
				caseId = notification.getCaseId();
		}

		public Long getCaseId() {
			return caseId;
		}

	}

	private Long id, userId, firmId, fileId;
	private String type, notes;
	private Timestamp time;

	public static NotificationResponse of(V_CaseNotification notification) {
		return new CaseNotificationResponse(notification);
	}

	public static NotificationResponse of(Notification notification) {
		if (notification.getCase() != null)
			return new CaseNotificationResponse(notification);
		else if (notification.getFile() != null)
			return new FileNotificationResponse(notification);
		else
			return new NotificationResponse(notification);
	}

	private NotificationResponse(Notification notification) {
		id = notification.getId();
		userId = ifNotNull(notification.getUser(), User::getId);
		firmId = ifNotNull(notification.getFirm(), Firm::getId);
		fileId = ifNotNull(notification.getFile(), File::getId);
		type = notification.getType().toString();
		notes = notification.getNotes();
		time = notification.getCreationTime();
	}

	public NotificationResponse(V_CaseNotification notification) {
		id = notification.getId();
		userId = notification.getUserId();
		firmId = notification.getFirmId();
		fileId = notification.getFileId();
		type = notification.getType().toString();
		notes = notification.getNotes();
		time = notification.getCre_dat();
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
