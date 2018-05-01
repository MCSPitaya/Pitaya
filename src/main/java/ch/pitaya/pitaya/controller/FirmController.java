package ch.pitaya.pitaya.controller;

import java.util.List;

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
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.PatchFirmRequest;
import ch.pitaya.pitaya.payload.request.SignUpRequest;
import ch.pitaya.pitaya.payload.response.ApiResponse;
import ch.pitaya.pitaya.payload.response.FirmSummary;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.payload.response.UserSummary;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.FirmService;
import ch.pitaya.pitaya.service.UserService;
import ch.pitaya.pitaya.util.Utils;

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
	@Authorize(AuthCode.USER_CREATE)
	public ResponseEntity<?> addUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		Firm firm = securityFacade.getCurrentFirm();
		userService.createUser(signUpRequest, firm);
		return ResponseEntity.ok(new ApiResponse("user created"));
	}

	@GetMapping("/info")
	public FirmSummary getFirmInfo() {
		return new FirmSummary(securityFacade.getCurrentFirm());
	}

	@PatchMapping("/info")
	@Authorize(AuthCode.FIRM_MODIFY)
	public ResponseEntity<?> patchFirmInfo(@RequestBody PatchFirmRequest request) {
		firmService.editFirm(request, securityFacade.getCurrentFirm());
		return SimpleResponse.ok("Update successful");
	}

	@GetMapping("/users")
	public List<UserSummary> getFirmUsers() {
		List<User> users = securityFacade.getCurrentFirm().getUsers();
		return Utils.map(users, UserSummary::new);
	}
}
