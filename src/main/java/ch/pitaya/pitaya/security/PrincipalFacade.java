package ch.pitaya.pitaya.security;

public interface PrincipalFacade {

	UserPrincipal getCurrentUser();

	Long getCurrentUserId();

}
