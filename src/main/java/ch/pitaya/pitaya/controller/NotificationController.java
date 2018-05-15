package ch.pitaya.pitaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.exception.NotImplementedException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.Notification;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.response.NotificationResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.NotificationService;
import ch.pitaya.pitaya.util.Utils;

@RestController
@RequestMapping("/api")
public class NotificationController {

	@Autowired
	SecurityFacade securityFacade;

	@Autowired
	NotificationService notificationService;

	@Autowired
	CaseRepository caseRepository;

	@Autowired
	FileRepository fileRepository;

	@GetMapping("/user/changes")
	public List<NotificationResponse> getUserNotifications(//
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "0") int size) {
		User user = securityFacade.getCurrentUser();
		List<Notification> notifications = notificationService.getUserNotifications(user, page, size);
		return Utils.map(notifications, NotificationResponse::new);
	}

	@GetMapping("/firm/changes")
	public List<NotificationResponse> getFirmNotifications(//
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "0") int size) {
		Firm firm = securityFacade.getCurrentFirm();
		List<Notification> notifications = notificationService.getFirmNotifications(firm, page, size);
		return Utils.map(notifications, NotificationResponse::new);
	}

	@GetMapping("/case/{id}/changes")
	@AuthorizeCase(AuthCode.CASE_READ)
	public List<NotificationResponse> getCaseNotifications(//
			@PathVariable("id") long caseId, //
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "0") int size) {
		Firm firm = securityFacade.getCurrentFirm();
		Case case_ = caseRepository.findByIdAndFirm(caseId, firm)
				.orElseThrow(() -> new ResourceNotFoundException("case", "id", caseId));
		List<Notification> notifications = notificationService.getCaseNotifications(case_, page, size);
		return Utils.map(notifications, NotificationResponse::new);
	}

	@GetMapping("/file/{id}/changes")
	@AuthorizeFile(AuthCode.FILE_READ)
	public List<NotificationResponse> getFileNotifications(//
			@PathVariable("id") long fileId, //
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "0") int size) {
		throw new NotImplementedException("file notifications currently not supported");
		/*
		Firm firm = securityFacade.getCurrentFirm();
		File file = fileRepository.findByIdAndTheCaseFirm(fileId, firm)
				.orElseThrow(() -> new ResourceNotFoundException("file", "id", fileId));
		List<Notification> notifications = notificationService.getFileNotifications(file, page, size);
		return Utils.map(notifications, NotificationResponse::new);
		*/
	}

}
