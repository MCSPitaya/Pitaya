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
		return test(authCodes, user.getAuthCodes());
	}

	public boolean test(Case caze, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(authCodes, caze.getAuthCodes(user), user.getAuthCodes());
	}

	public boolean test(File file, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(authCodes, file.getAuthCodes(user), file.getCase().getAuthCodes(user), user.getAuthCodes());
	}

	private boolean test(AuthCode[] need, String... codes) {
		for (String code : codes) {
			List<AuthCode> list = resolver.decode(code);
			if (list.isEmpty())
				continue;
			return doTest(list, need);
		}
		return false;
	}

	private boolean doTest(List<AuthCode> have, AuthCode[] need) {
		for (AuthCode code : need) {
			if (!have.contains(code)) {
				return false;
			}
		}
		return true;
	}

}
