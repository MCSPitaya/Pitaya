package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;

import ch.pitaya.pitaya.model.File;

public class FileDetails {

	private Long id;
	private String name;
	private int revisions;
	private Timestamp created, modified;

	public FileDetails(File f) {
		this.id = f.getId();
		this.name = f.getName();
		this.revisions = f.getFileData().size();
		created = f.getCreationTime();
		modified = f.getModificationTime();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRevisions() {
		return revisions;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getModified() {
		return modified;
	}

}
