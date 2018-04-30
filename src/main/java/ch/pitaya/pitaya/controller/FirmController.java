package ch.pitaya.pitaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.ApiResponse;
import ch.pitaya.pitaya.payload.FirmSummary;
import ch.pitaya.pitaya.payload.SignUpRequest;
import ch.pitaya.pitaya.payload.UserSummary;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.UserService;

@RestController
@RequestMapping("/api/firm")
public class FirmController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityFacade securityFacade;

	@PostMapping("/addUser")
	public ResponseEntity<?> addUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		Firm firm = securityFacade.getCurrentFirm();
		userService.createUser(signUpRequest, firm);
		return ResponseEntity.ok(new ApiResponse("user created"));
	}
	
	@GetMapping("/info")
	public FirmSummary getFirmInfo( ) {
		return new FirmSummary(securityFacade.getCurrentFirm());
	}
}
