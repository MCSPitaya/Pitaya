package ch.pitaya.pitaya.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.model.Notification;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.ChangePasswordRequest;
import ch.pitaya.pitaya.payload.response.NotificationResponse;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.payload.response.UserSummary;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.NotificationService;
import ch.pitaya.pitaya.service.UserService;
import ch.pitaya.pitaya.util.Utils;

@RequestMapping("/api/user")
@RestController
public class UserController {

	@Autowired
	SecurityFacade securityFacade;

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserService userService;

	@GetMapping("/info")
	public UserSummary getCurrentUser() {
		return new UserSummary(securityFacade.getCurrentUser());
	}

	@PostMapping("/password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		User user = securityFacade.getCurrentUser();
		userService.changePassword(user, request);
		return SimpleResponse.ok("password changed");
	}

	@GetMapping("/changes")
	public List<NotificationResponse> getNotifications(//
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "0") int size) {
		User user = securityFacade.getCurrentUser();
		List<Notification> notifications = notificationService.getUserNotifications(user, page, size);
		return Utils.map(notifications, NotificationResponse::new);
	}

}
