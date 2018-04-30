package ch.pitaya.pitaya.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SimpleResponse {

	private String message;

	private SimpleResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public static ResponseEntity<?> create(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new SimpleResponse(message));
	}

	public static ResponseEntity<?> create(int status, String message) {
		return ResponseEntity.status(status).body(new SimpleResponse(message));
	}

	public static ResponseEntity<?> ok(String message) {
		return ResponseEntity.ok(new SimpleResponse(message));
	}

}
