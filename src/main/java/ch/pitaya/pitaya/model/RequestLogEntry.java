package ch.pitaya.pitaya.model;

import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

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

	private String agent;

	private String method;

	private String params;

	protected RequestLogEntry() {
		// JPA
	}

	public RequestLogEntry(HttpServletRequest request, boolean hasToken, Long userId) {
		ip = request.getRemoteAddr();
		endpoint = request.getServletPath();
		this.token = hasToken;
		this.passed = userId != null;
		this.userId = userId;
		this.method = request.getMethod();
		this.agent = request.getHeader("User-Agent");
		this.params = request.getParameterMap().entrySet().stream().flatMap(entry -> {
			String key = entry.getKey();
			return Arrays.stream(entry.getValue()).map(elem -> key + "=" + elem);
		}).reduce(null, (a, x) -> {
			if (a == null)
				return x;
			else
				return a + "&" + x;
		});
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

	public String getAgent() {
		return agent;
	}

	public String getMethod() {
		return method;
	}

	public String getParams() {
		return params;
	}

}
