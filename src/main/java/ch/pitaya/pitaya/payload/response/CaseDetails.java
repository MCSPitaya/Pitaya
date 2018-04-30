package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.Case;

public class CaseDetails {

	private Long id;
	private String number;
	private String title;
	private String description;

	public CaseDetails(Case c) {
		this.id = c.getId();
		this.number = c.getCaseNumber();
		this.title = c.getTitle();
		this.description = c.getDescription();
	}

	public Long getId() {
		return id;
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