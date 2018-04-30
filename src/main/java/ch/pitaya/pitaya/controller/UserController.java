package ch.pitaya.pitaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.ChangePasswordRequest;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.payload.response.UserSummary;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.UserService;

@RequestMapping("/api/user")
@RestController
public class UserController {

	@Autowired
	SecurityFacade securityFacade;

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

}
