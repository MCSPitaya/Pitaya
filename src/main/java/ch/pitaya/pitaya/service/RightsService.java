package ch.pitaya.pitaya.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.AuthCodeResolver;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.response.AuthCodeResponse;

@Service
public class RightsService {
	
	@Autowired
	private AuthCodeResolver resolver;

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

	public AuthCodeResponse getAuthCodes(User user, File f) {
		HashMap<String, List<AuthCode>> ex = getAuthCodes(user, f, true);
		HashMap<String, List<AuthCode>> im = getAuthCodes(user, f, false);
		return new AuthCodeResponse(ex, im);
	}

	private HashMap<String, List<AuthCode>> getAuthCodes(User user, boolean explicit) {
		HashMap<String, List<AuthCode>> map = new HashMap<>();
		System.out.println(user.getAuthCodes());
		System.out.println(resolver);
		System.out.println(resolver.getUserCodes(user.getAuthCodes(), explicit));
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

	private HashMap<String, List<AuthCode>> getAuthCodes(User user, File f, boolean explicit) {
		HashMap<String, List<AuthCode>> map = new HashMap<>();
		if (explicit) {
			map.put("file", resolver.getFileCodes(f.getAuthCodes(user), explicit));
			return map;
		} else {
			map.put("file", resolver.decode(user.getAuthCodes(), f.getCase().getAuthCodes(user), f.getAuthCodes(user),
					resolver::isFileCode));
		}
		return map;
	}

	@AuthorizeCase(AuthCode.CASE_READ_ROLES)
	public AuthCodeResponse getAuthCodesSafe(User u, Case c) {
		return getAuthCodes(u, c);
	}

	@AuthorizeFile(AuthCode.FILE_READ_ROLES)
	public AuthCodeResponse getAuthCodesSafe(User u, File f) {
		return getAuthCodes(u, f);
	}

}
