package ch.pitaya.pitaya.authorization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.pitaya.pitaya.exception.AuthorizationException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
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
		if (!test(authCodes))
			throw new AuthorizationException(authCodes);
	}

	public void require(Case caze, AuthCode... authCodes) {
		if (!test(caze, authCodes))
			throw new AuthorizationException(authCodes);
	}

	public void require(File file, AuthCode... authCodes) {
		if (!test(file, authCodes))
			throw new AuthorizationException(authCodes);
	}

	public boolean test(AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(authCodes, user.getAuthCodes(), null, null);
	}

	public boolean test(Case caze, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(authCodes, user.getAuthCodes(), caze.getAuthCodes(user), null);
	}

	public boolean test(File file, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(authCodes, user.getAuthCodes(), file.getCase().getAuthCodes(user), file.getAuthCodes(user));
	}

	private boolean test(AuthCode[] need, String _user, String _case, String _file) {
		List<AuthCode> codes = resolver.decode(_user, _case, _file);
		for (AuthCode code : need) {
			if (!codes.contains(code)) {
				return false;
			}
		}
		return true;
	}

}
