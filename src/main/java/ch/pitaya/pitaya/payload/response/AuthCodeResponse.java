package ch.pitaya.pitaya.payload.response;

import java.util.HashMap;
import java.util.List;

import ch.pitaya.pitaya.authorization.AuthCode;

public class AuthCodeResponse {

	private HashMap<String, List<AuthCode>> explicit;
	private HashMap<String, List<AuthCode>> implicit;

	public AuthCodeResponse(HashMap<String, List<AuthCode>> ex, HashMap<String, List<AuthCode>> im) {
		this.explicit = ex;
		this.implicit = im;
	}

	public HashMap<String, List<AuthCode>> getExplicit() {
		return explicit;
	}

	public HashMap<String, List<AuthCode>> getImplicit() {
		return implicit;
	}

}
