package ch.pitaya.pitaya.payload.request;

import javax.validation.constraints.NotBlank;

public class CreateCaseRequest {

	private long courtId;
	
	@NotBlank
	private String title;

	@NotBlank
	private String number;

	private String description;

	public String getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public long getCourtId() {
		return courtId;
	}
	
}
