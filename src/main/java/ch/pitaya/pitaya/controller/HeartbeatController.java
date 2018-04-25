package ch.pitaya.pitaya.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.ApiResponse;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class HeartbeatController {

	@GetMapping("/heartbeat")
	public ResponseEntity<?> heartbeat() {
		return ResponseEntity.ok(new ApiResponse("heartbeat"));
	}

}
