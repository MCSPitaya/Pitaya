package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.security.Token;
import ch.pitaya.pitaya.security.TokenProviderFacade;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProviderFacade tokenProviderFacade;

	/**
	 * Logs in the user and updates the security context
	 * 
	 * @param user
	 *            the username or email of the user
	 * @param password
	 *            the password of the user
	 */
	public void login(String user, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * Creates a new Token from the authentication in the current security context
	 */
	public Token generateToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return tokenProviderFacade.generateTokenPair(authentication);
	}

}
