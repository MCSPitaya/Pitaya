package ch.pitaya.pitaya.authorization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.pitaya.pitaya.exception.AuthorizationException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.V_CaseAuthRepository;
import ch.pitaya.pitaya.repository.V_FileAuthRepository;
import ch.pitaya.pitaya.security.SecurityFacade;

@Component
public class Authorization {

	@Autowired
	private AuthCodeResolver resolver;

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private V_FileAuthRepository fileAuthRepo;
	@Autowired
	private V_CaseAuthRepository caseAuthRepo;

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
		return test(user.getAuthCodes(), null, null, authCodes);
	}

	public boolean test(Case caze, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(user.getAuthCodes(), caze.getAuthCodes(user), null, authCodes);
	}

	public boolean test(File file, AuthCode... authCodes) {
		User user = securityFacade.getCurrentUser();
		return test(user.getAuthCodes(), file.getCases().stream().map(c -> c.getAuthCodes(user)).reduce((a, b) -> {
			if (a == null)
				return b;
			else if (b == null)
				return a;
			else
				return a + "," + b;
		}).orElse(null), file.getAuthCodes(user), authCodes);
	}

	public boolean test(AuthCodeProvider provider, AuthCode... need) {
		return test(provider.getUserAuthCodes(), provider.getCaseAuthCodes(), provider.getFileAuthCodes(), need);
	}

	public boolean test(String _user, String _case, String _file, AuthCode... need) {
		List<AuthCode> codes = resolver.decode(_user, _case, _file);
		for (AuthCode code : need) {
			if (!codes.contains(code)) {
				return false;
			}
		}
		return true;
	}

	public void requireCase(Long caseId, AuthCode[] codes) {
		Long userId = securityFacade.getCurrentUserId();
		boolean authorized = caseAuthRepo.findByIdAndUserId(caseId, userId) //
				.map(ca -> test(ca, codes)) //
				.orElseThrow(() -> new ResourceNotFoundException("case", "id", caseId));
		if (!authorized)
			throw new AuthorizationException(codes);
	}

	public void requireFile(Long fileId, AuthCode[] codes) {
		Long userId = securityFacade.getCurrentUserId();
		boolean authorized = fileAuthRepo.findByIdAndUserId(fileId, userId) //
				.map(fa -> test(fa, codes)) //
				.orElseThrow(() -> new ResourceNotFoundException("file", "id", fileId));
		if (!authorized)
			throw new AuthorizationException(codes);
	}

}
