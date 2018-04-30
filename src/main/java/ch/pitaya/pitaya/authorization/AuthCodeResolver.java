package ch.pitaya.pitaya.authorization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthCodeResolver {

	public List<AuthCode> decode(String codes) {
		if (StringUtils.isEmpty(codes))
			return Collections.emptyList();
		List<AuthCode> list = new ArrayList<>();
		for (String code : codes.split(",")) {
			AuthCode auth = AuthCode.valueOf(code);
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
