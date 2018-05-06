package ch.pitaya.pitaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.request.LoginRequest;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.security.TokenPair;
import ch.pitaya.pitaya.service.AuthService;
import ch.pitaya.pitaya.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		authService.login(loginRequest.getUser(), loginRequest.getPassword());
		TokenPair token = tokenService.generateTokenPair();
		return ResponseEntity.ok(token);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken() {
		TokenPair token = tokenService.replaceToken();
		return ResponseEntity.ok(token);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser() {
		tokenService.revokeToken();
		return SimpleResponse.ok("logout successful");
	}

}
