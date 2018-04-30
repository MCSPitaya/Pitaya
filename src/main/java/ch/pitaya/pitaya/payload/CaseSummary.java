package ch.pitaya.pitaya.payload;

import ch.pitaya.pitaya.model.Case;

public class CaseSummary {

	private Long id;
	private String number;
	private String title;

	public CaseSummary(Long id, String number, String title) {
		this.id = id;
		this.number = number;
		this.title = title;
	}

	public CaseSummary(Case c) {
		this.id = c.getId();
		this.number = c.getCaseNumber();
		this.title = c.getTitle();
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

}
