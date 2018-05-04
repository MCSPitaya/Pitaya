package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import ch.pitaya.pitaya.model.File;

public class FileSummary {

	private Long id;
	private String title;
	private Timestamp created, modified;

	public FileSummary(File file) {
		id = file.getId();
		title = file.getName();
		created = file.getCreationTime();
		modified = file.getModificationTime();
	}

	public Long getId() {
		return id;
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
