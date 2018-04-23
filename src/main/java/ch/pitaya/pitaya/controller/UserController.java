package ch.pitaya.pitaya.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.UserSummary;
import ch.pitaya.pitaya.security.CurrentUser;
import ch.pitaya.pitaya.security.UserPrincipal;

@Secured("ROLE_USER")
@RestController
public class UserController {

	@GetMapping("/user/me")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

}
