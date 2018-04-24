package ch.pitaya.pitaya.security;

public class Token {
	private String accessToken;
	private String tokenType = "Bearer";

	public Token(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}
}
