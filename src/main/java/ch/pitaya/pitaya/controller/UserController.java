package ch.pitaya.pitaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.UserSummary;
import ch.pitaya.pitaya.security.PrincipalFacade;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class UserController {

	@Autowired
	PrincipalFacade principalFacade;

	@GetMapping("/user/me")
	public UserSummary getCurrentUser() {
		return new UserSummary(principalFacade.getCurrentUser());
	}

}
