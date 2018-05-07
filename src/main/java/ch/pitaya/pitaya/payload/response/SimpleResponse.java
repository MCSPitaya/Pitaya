package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SimpleResponse {

	private String message;
	private Timestamp time = new Timestamp(System.currentTimeMillis());

	private SimpleResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getTime() {
		return time;
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
	
	public static ResponseEntity<?> ok() {
		return ResponseEntity.noContent().build();
	}

}
