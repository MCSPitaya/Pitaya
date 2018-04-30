package ch.pitaya.pitaya.security;

import org.springframework.security.core.Authentication;

public interface TokenProvider {

	RawToken generateToken(Authentication authentication);

	Long getUserIdFromToken(String token);

	boolean validateToken(String authToken);

}
