package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "request_log")
public class RequestLogEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String ip;

	private String endpoint;

	private Boolean token;

	private Boolean passed;

	private Long userId;

	private Timestamp timestamp;

	protected RequestLogEntry() {
		// JPA
	}

	public RequestLogEntry(String ip, String endpoint, boolean token, Long userId) {
		this.ip = ip;
		this.endpoint = endpoint;
		this.token = token;
		this.passed = userId != null;
		this.userId = userId;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Boolean getPassed() {
		return passed;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Boolean getToken() {
		return token;
	}

	public Long getUserId() {
		return userId;
	}

}
