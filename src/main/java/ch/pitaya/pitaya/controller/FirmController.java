package ch.pitaya.pitaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.PatchFirmRequest;
import ch.pitaya.pitaya.payload.request.SignUpRequest;
import ch.pitaya.pitaya.payload.response.ApiResponse;
import ch.pitaya.pitaya.payload.response.FirmSummary;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.FirmService;
import ch.pitaya.pitaya.service.UserService;

@RestController
@RequestMapping("/api/firm")
public class FirmController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityFacade securityFacade;
	
	@Autowired
	private FirmService firmService;

	@Autowired
	Authorization authorization;

	@PostMapping("/addUser")
	public ResponseEntity<?> addUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		authorization.require(AuthCode.USER_CREATE);
		Firm firm = securityFacade.getCurrentFirm();
		userService.createUser(signUpRequest, firm);
		return ResponseEntity.ok(new ApiResponse("user created"));
	}

	@GetMapping("/info")
	public FirmSummary getFirmInfo() {
		return new FirmSummary(securityFacade.getCurrentFirm());
	}
	
	@PatchMapping("/info")
	public ResponseEntity<?> patchFirmInfo(@RequestBody PatchFirmRequest request) {
		firmService.editFirm(request, securityFacade.getCurrentFirm());
		return SimpleResponse.ok("Update successful"); 
	}
}
