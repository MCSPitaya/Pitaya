package ch.pitaya.pitaya.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalFacade {

	public UserPrincipal getCurrentUser() {
		return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

}
