package ch.pitaya.pitaya.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.AuthCodeResolver;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.model.V_FileAuth;
import ch.pitaya.pitaya.payload.request.AuthCodeChangeRequest;
import ch.pitaya.pitaya.payload.response.AuthCodeResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.repository.UserRepository;

@Service
public class RightsService {

	@Autowired
	private AuthCodeResolver resolver;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CaseRepository caseRepo;
	@Autowired
	private FileRepository fileRepo;

	public AuthCodeResponse getAuthCodes(User user) {
		HashMap<String, List<AuthCode>> ex = getAuthCodes(user, true);
		HashMap<String, List<AuthCode>> im = getAuthCodes(user, false);
		return new AuthCodeResponse(ex, im);
	}

	public AuthCodeResponse getAuthCodes(User user, Case c) {
		HashMap<String, List<AuthCode>> ex = getAuthCodes(user, c, true);
		HashMap<String, List<AuthCode>> im = getAuthCodes(user, c, false);
		return new AuthCodeResponse(ex, im);
	}

	public AuthCodeResponse getAuthCodes(V_FileAuth f) {
		HashMap<String, List<AuthCode>> ex = getAuthCodes(f, true);
		HashMap<String, List<AuthCode>> im = getAuthCodes(f, false);
		return new AuthCodeResponse(ex, im);
	}

	private HashMap<String, List<AuthCode>> getAuthCodes(User user, boolean explicit) {
		HashMap<String, List<AuthCode>> map = new HashMap<>();
		map.put("firm", resolver.getUserCodes(user.getAuthCodes(), explicit));
		map.put("case", resolver.getCaseCodes(user.getAuthCodes(), explicit));
		map.put("file", resolver.getFileCodes(user.getAuthCodes(), explicit));
		return map;
	}

	private HashMap<String, List<AuthCode>> getAuthCodes(User user, Case c, boolean explicit) {
		HashMap<String, List<AuthCode>> map = new HashMap<>();
		if (explicit) {
			map.put("case", resolver.getCaseCodes(c.getAuthCodes(user), explicit));
			map.put("file", resolver.getFileCodes(c.getAuthCodes(user), explicit));
			return map;
		} else {
			map.put("case", resolver.decode(user.getAuthCodes(), c.getAuthCodes(user), null, resolver::isCaseCode));
			map.put("file", resolver.decode(user.getAuthCodes(), c.getAuthCodes(user), null, resolver::isFileCode));
		}
		return map;
	}

	private HashMap<String, List<AuthCode>> getAuthCodes(V_FileAuth f, boolean explicit) {
		HashMap<String, List<AuthCode>> map = new HashMap<>();
		if (explicit) {
			map.put("file", resolver.getFileCodes(f.getFileAuthCodes(), explicit));
			return map;
		} else {
			map.put("file", resolver.decode(f.getUserAuthCodes(), f.getCaseAuthCodes(), f.getFileAuthCodes(),
					resolver::isFileCode));
		}
		return map;
	}

	public void setAuthCodesSafe(AuthCodeChangeRequest r, User u) {
		u.setAuthCodes(r.codes);
		userRepo.save(u);
	}

	public void setAuthCodes(AuthCodeChangeRequest r, User u, Case c) {
		c.setAuthCodes(u, r.codes);
		caseRepo.save(c);
	}

	public void setAuthCodes(AuthCodeChangeRequest r, User u, File f) {
		f.setAuthCodes(u, r.codes);
		fileRepo.save(f);
	}

}
