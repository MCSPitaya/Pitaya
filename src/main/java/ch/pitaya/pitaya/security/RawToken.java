package ch.pitaya.pitaya.security;

import java.sql.Timestamp;

public class RawToken {

	private String token;
	private Timestamp creationDate;
	private Timestamp expirationDate;

	public RawToken(String token, Timestamp creationDate, Timestamp expirationDate) {
		this.token = token;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	
	public Timestamp getCreationDate() {
		return creationDate;
	}

	public String getToken() {
		return token;
	}

}
