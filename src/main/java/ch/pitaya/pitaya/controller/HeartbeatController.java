package ch.pitaya.pitaya.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.SSEService;

@RestController
public class HeartbeatController {

	@Autowired
	private SSEService sse;
	@Autowired
	private SecurityFacade security;

	@GetMapping("/api/heartbeat")
	public ResponseEntity<?> heartbeat() {
		return SimpleResponse.ok("heartbeat");
	}

	@Scheduled(fixedRate = 10000L)
	public void sendSSE() {
		sse.emit(1L, "heartbeat", "tick", new Date());
	}

	@GetMapping("/api/heartbeat/events")
	public SseEmitter getEventStream() {
		Long firmId = security.getCurrentFirmId();
		return sse.create(firmId, "heartbeat");
	}

}
