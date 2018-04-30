package ch.pitaya.pitaya.payload.request;

import javax.validation.constraints.NotBlank;

public class CreateCaseRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String number;

	private String description;

	public CreateCaseRequest(String number, String title, String description) {
		this.title = title;
		this.number = number;
		this.description = description;
	}

	public String getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

}
