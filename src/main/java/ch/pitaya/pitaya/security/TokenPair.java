package ch.pitaya.pitaya.security;

public class TokenPair {

	private String accessToken;
	private String refreshToken;

	public TokenPair(RawToken accessToken, RawToken refreshToken) {
		this.accessToken = accessToken.getToken();
		this.refreshToken = refreshToken.getToken();
	}
	
	public TokenPair(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
