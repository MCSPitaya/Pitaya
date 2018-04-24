package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.SignUpRequest;
import ch.pitaya.pitaya.repository.UserRepository;
import ch.pitaya.pitaya.security.JwtTokenProvider;
import ch.pitaya.pitaya.security.Token;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

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
	 * Registers a new user from the given request
	 * 
	 * @param request
	 *            the request holding the user data
	 * @throws BadRequestException
	 *             if the username or email is already taken
	 */
	public void register(SignUpRequest request) {
		if (userRepository.existsByUsername(request.getUsername()))
			throw new BadRequestException("Username is already taken!");

		if (userRepository.existsByEmail(request.getEmail()))
			throw new BadRequestException("Email Address already in use!");

		// Creating user's account
		User user = new User(request.getName(), request.getUsername(), request.getEmail(),
				passwordEncoder.encode(request.getPassword()));

		userRepository.saveAndFlush(user);
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
