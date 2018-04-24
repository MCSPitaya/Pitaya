package ch.pitaya.pitaya.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.payload.ApiResponse;

@RestController
public class HeartbeatController {

	@GetMapping("/heartbeat")
	public ResponseEntity<?> heartbeat() {
		return ResponseEntity.ok(new ApiResponse("heartbeat"));
	}

}