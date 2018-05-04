package ch.pitaya.pitaya.authorization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthCodeResolver {

	private final HashSet<AuthCode> userCodes = new HashSet<>();
	private final HashSet<AuthCode> caseCodes = new HashSet<>();
	private final HashSet<AuthCode> fileCodes = new HashSet<>();

	public AuthCodeResolver() {
		// load code maps
		for (AuthCode code : AuthCode.values()) {
			String name = code.toString();
			if (name.startsWith("CASE_"))
				caseCodes.add(code);
			else if (name.startsWith("FILE_"))
				fileCodes.add(code);
			else
				userCodes.add(code);
		}

	}

	public List<AuthCode> getUserCodes(String raw, boolean explicitOnly) {
		List<AuthCode> codes = decode(raw, explicitOnly);
		codes.removeAll(caseCodes);
		codes.removeAll(fileCodes);
		return codes;
	}

	public List<AuthCode> getCaseCodes(String raw, boolean explicitOnly) {
		List<AuthCode> codes = decode(raw, explicitOnly);
		codes.removeAll(userCodes);
		codes.removeAll(fileCodes);
		return codes;
	}

	public List<AuthCode> getFileCodes(String raw, boolean explicitOnly) {
		List<AuthCode> codes = decode(raw, explicitOnly);
		codes.removeAll(userCodes);
		codes.removeAll(caseCodes);
		return codes;
	}

	public boolean isUserCode(AuthCode code) {
		return userCodes.contains(code);
	}

	public boolean isCaseCode(AuthCode code) {
		return caseCodes.contains(code);
	}

	public boolean isFileCode(AuthCode code) {
		return fileCodes.contains(code);
	}

	public List<AuthCode> decode(String _user, String _case, String _file, Predicate<AuthCode> filter) {
		return decode(_user, _case, _file).stream().filter(filter).collect(Collectors.toList());
	}

	// TODO: caching
	public List<AuthCode> decode(String _user, String _case, String _file) {
		List<AuthCode> codes = decode(_user, false);
		if (_case != null) {
			codes.removeAll(caseCodes);
			codes.removeAll(fileCodes);
			List<AuthCode> c = decode(_case, false);
			c.removeAll(userCodes);
			codes.addAll(c);
		}
		if (_file != null) {
			codes.removeAll(fileCodes);
			List<AuthCode> c = decode(_file, false);
			c.removeAll(userCodes);
			c.removeAll(caseCodes);
			codes.addAll(c);
		}
		return codes;
	}

	public List<AuthCode> decode(String codes, boolean explicitOnly) {
		if (StringUtils.isEmpty(codes))
			return Collections.emptyList();
		List<AuthCode> list = new ArrayList<>();
		for (String code : codes.split(",")) {
			AuthCode auth = AuthCode.valueOf(code);
			if (explicitOnly)
				list.add(auth);
			else
				addRecursively(auth, list);
		}
		return list;
	}

	private void addRecursively(AuthCode code, List<AuthCode> list) {
		if (list.contains(code))
			return;
		list.add(code);
		for (AuthCode c : code.parents)
			addRecursively(c, list);
	}

	public String encode(List<AuthCode> codes) {
		if (codes.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		int size = codes.size() - 1;
		for (int i = 0; i < size; i++)
			sb.append(codes.get(i).toString());
		sb.append(codes.get(size));
		return sb.toString();
	}

}
