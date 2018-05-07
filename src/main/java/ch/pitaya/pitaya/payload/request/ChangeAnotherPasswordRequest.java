package ch.pitaya.pitaya.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangeAnotherPasswordRequest {

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;

	@NotBlank
	@Size(min = 6, max = 20)
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public String getPassword() {
		return password;
	}

}
