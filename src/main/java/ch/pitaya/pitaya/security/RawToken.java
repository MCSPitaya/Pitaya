package ch.pitaya.pitaya.security;

import java.sql.Timestamp;

public class RawToken {

	private String token;
	private Timestamp expirationDate;

	public RawToken(String token, Timestamp expirationDate) {
		this.token = token;
		this.expirationDate = expirationDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public String getToken() {
		return token;
	}

}
