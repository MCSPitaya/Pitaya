package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.ChangePasswordRequest;
import ch.pitaya.pitaya.payload.request.SignUpRequest;
import ch.pitaya.pitaya.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new user from the given request
	 * 
	 * @param request
	 *            the request holding the user data
	 * @throws BadRequestException
	 *             if the username or email is already taken
	 */
	public void createUser(SignUpRequest request, Firm firm) {
		if (userRepository.existsByUsername(request.getUsername()))
			throw new BadRequestException("Username is already taken!");

		if (userRepository.existsByEmail(request.getEmail()))
			throw new BadRequestException("Email Address already in use!");

		
		// Creating user's account
		User user = new User(request.getName(), request.getUsername(), request.getEmail(),
				passwordEncoder.encode(request.getPassword()), firm, "BASIC_USER");
		
		userRepository.saveAndFlush(user);
	}

	public void changePassword(User user, ChangePasswordRequest request) {
		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
			throw new BadRequestException("incorrect password");
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

}
