package ch.pitaya.pitaya.authorization;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AuthCodeProvider {

	@JsonIgnore
	String getFileAuthCodes();

	@JsonIgnore
	String getCaseAuthCodes();

	@JsonIgnore
	String getUserAuthCodes();

}
