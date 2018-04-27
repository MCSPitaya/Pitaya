package ch.pitaya.pitaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.UserSummary;
import ch.pitaya.pitaya.security.PrincipalFacade;

@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	PrincipalFacade principalFacade;

	@GetMapping("/user/me")
	public UserSummary getCurrentUser() {
		return new UserSummary(principalFacade.getCurrentUser());
	}

}
