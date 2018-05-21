package ch.pitaya.pitaya.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.Notification;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.model.V_CaseNotification;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.repository.NotificationRepository;
import ch.pitaya.pitaya.repository.V_CaseNotificationRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.util.Utils;

@Service
public class NotificationService {

	@Autowired
	private SecurityFacade security;

	@Autowired
	private Authorization auth;

	@Autowired
	private NotificationRepository repo;

	@Autowired
	private V_CaseNotificationRepository caseNotRepo;

	@Autowired
	private FileRepository fileRepo;

	public void add(NotificationType type) {
		add(type, null, null, null);
	}

	public void add(NotificationType type, String notes) {
		add(type, null, null, notes);
	}

	public void add(NotificationType type, Case theCase) {
		add(type, theCase, null, null);
	}

	public void add(NotificationType type, Case theCase, String notes) {
		add(type, theCase, null, notes);
	}

	public void add(NotificationType type, User user, Case theCase) {
		add(type, user, theCase.getFirm(), theCase, null, null, new Timestamp(new Date().getTime()));
	}

	public void add(NotificationType type, File file) {
		add(type, null, file, null);
	}

	public void add(NotificationType type, File file, String notes) {
		add(type, null, file, notes);
	}

	public void add(NotificationType type, Case theCase, File file, String notes) {
		User user = security.getCurrentUser();
		add(type, user, user.getFirm(), theCase, file, notes, new Timestamp(new Date().getTime()));
	}

	public void add(NotificationType type, User user, Firm firm, Case theCase, File file, String notes,
			Timestamp time) {
		Notification notification = new Notification(type, user, firm, theCase, file, time, notes);
		repo.save(notification);
	}

	public List<Notification> getUserNotifications(User user, int page, int size) {
		return Utils.paginate(user.getNotifications(), page, size);
	}

	public List<Notification> getFirmNotifications(Firm firm, int page, int size) {
		return Utils.paginate(firm.getNotifications(), page, size, n -> {
			File f = n.getFile();
			Case c = n.getCase();
			if (f != null) {
				return auth.test(f, AuthCode.FILE_READ);
			} else if (c != null) {
				return auth.test(c, AuthCode.CASE_READ);
			} else {
				return true;
			}
		});
	}

	public List<V_CaseNotification> getCaseNotifications(Long caseId, int page, int size) {
		return Utils.paginate(caseNotRepo.findByCaseIdOrFileCaseId(caseId, caseId), page, size, n -> {
			Long fid = n.getFileId();
			if (fid != null) {
				File f = fileRepo.findById(fid).orElse(null);
				if (f != null)
					return auth.test(f, AuthCode.FILE_READ);
			}
			return true;
		});
	}

	public List<Notification> getFileNotifications(File file, int page, int size) {
		return Utils.paginate(file.getNotifications(), page, size);
	}

}
