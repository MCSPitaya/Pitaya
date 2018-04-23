package ch.pitaya.pitaya.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalFacadeImpl implements PrincipalFacade {

	@Override
	public UserPrincipal getCurrentUser() {
		return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

}
