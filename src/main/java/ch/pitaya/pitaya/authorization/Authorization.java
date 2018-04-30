package ch.pitaya.pitaya.authorization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.pitaya.pitaya.exception.AuthorizationException;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.security.SecurityFacade;

@Component
public class Authorization {

	@Autowired
	private AuthCodeResolver resolver;

	@Autowired
	private SecurityFacade securityFacade;

	/**
	 * Verifies that the currently logged in user has all the given authorization
	 * codes
	 * 
	 * @param authCodes
	 *            the codes that have to be satisfied
	 * @throws AuthorizationException
	 *             at least one auth code is missing
	 */
	public void require(AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		List<AuthCode> codes = resolver.decode(user.getAuthCodes());

		for (AuthCode code : authCodes) {
			if (!codes.contains(code)) {
				throw new AuthorizationException(authCodes);
			}
		}
	}

}
