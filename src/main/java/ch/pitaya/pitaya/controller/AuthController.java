package ch.pitaya.pitaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.ApiResponse;
import ch.pitaya.pitaya.payload.LoginRequest;
import ch.pitaya.pitaya.payload.UserIdentityAvailability;
import ch.pitaya.pitaya.repository.UserRepository;
import ch.pitaya.pitaya.security.Token;
import ch.pitaya.pitaya.service.AuthService;
import ch.pitaya.pitaya.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		authService.login(loginRequest.getUser(), loginRequest.getPassword());
		Token token = tokenService.generateTokenPair();
		return ResponseEntity.ok(token);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken() {
		Token token = tokenService.replaceToken();
		return ResponseEntity.ok(token);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser() {
		boolean removed = tokenService.revokeToken();
		return ResponseEntity.ok(new ApiResponse(removed, "logout successful"));
	}

	@GetMapping("/usernameAvailable")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/emailAvailable")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}
}
