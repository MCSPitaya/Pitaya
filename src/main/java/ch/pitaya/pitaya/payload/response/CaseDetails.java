package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import ch.pitaya.pitaya.model.Case;

public class CaseDetails {

	private Long id;
	private String number;
	private String title;
	private String description;
	private Timestamp created, modified;

	public CaseDetails(Case c) {
		this.id = c.getId();
		this.number = c.getCaseNumber();
		this.title = c.getTitle();
		this.description = c.getDescription();
		this.created = c.getCreationTime();
		this.modified = c.getModificationTime();
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

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getModified() {
		return modified;
	}

}