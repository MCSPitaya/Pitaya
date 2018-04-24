package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.security.JwtTokenProvider;
import ch.pitaya.pitaya.security.Token;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	/**
	 * Logs in the user and returns a valid JWT token
	 * 
	 * @param user
	 *            the username or email of the user
	 * @param password
	 *            the password
	 * @return a JWT token
	 */
	public Token login(String user, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new Token(
				tokenProvider.generateAccessToken(authentication),
				tokenProvider.generateRefreshToken(authentication));
	}

	/**
	 * Creates a new Token from the authentication in the current security context
	 */
	public Token refreshToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return new Token(
				tokenProvider.generateAccessToken(authentication),
				tokenProvider.generateRefreshToken(authentication));
	}

}
