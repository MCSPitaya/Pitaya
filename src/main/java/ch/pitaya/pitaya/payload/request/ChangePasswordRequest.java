package ch.pitaya.pitaya.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {

	@NotBlank
	@Size(min = 6, max = 20)
	private String oldPassword;

	@NotBlank
	@Size(min = 6, max = 20)
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

}
