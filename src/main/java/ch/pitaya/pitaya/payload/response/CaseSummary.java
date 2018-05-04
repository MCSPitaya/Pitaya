package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import ch.pitaya.pitaya.model.Case;

public class CaseSummary {

	private Long id;
	private String number;
	private String title;
	private Timestamp created, modified;

	public CaseSummary(Case c) {
		this.id = c.getId();
		this.number = c.getCaseNumber();
		this.title = c.getTitle();
		created = c.getCreationTime();
		modified = c.getModificationTime();
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

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getModified() {
		return modified;
	}

}
