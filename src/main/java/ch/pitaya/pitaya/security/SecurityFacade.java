package ch.pitaya.pitaya.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.UserRepository;

@Component
public class SecurityFacade {

	@Autowired
	private UserRepository userRepository;

	public UserPrincipal getCurrentPrincipal() {
		return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Long getCurrentUserId() {
		return getCurrentPrincipal().getId();
	}

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public String getCurrentToken() {
		return getCurrentPrincipal().getToken();
	}

	public User getCurrentUser() {
		return userRepository.findById(getCurrentUserId()).get();
	}

	public Firm getCurrentFirm() {
		return getCurrentUser().getFirm();
	}

	public Long getCurrentFirmId() {
		return getCurrentFirm().getId();
	}

}
