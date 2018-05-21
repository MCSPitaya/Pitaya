package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;
import java.util.List;

import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.V_CaseSummary;

public class FileDetails {

	private Long id;
	private String name;
	private int revisions;
	private Timestamp created, modified;
	private UserSummary createdBy, modifiedBy;
	private List<V_CaseSummary> cases;

	public FileDetails(File f, List<V_CaseSummary> cases) {
		this.id = f.getId();
		this.name = f.getName();
		this.revisions = f.getFileData().size();
		created = f.getCreationTime();
		modified = f.getModificationTime();
		this.createdBy = new UserSummary(f.getCreationUser());
		this.modifiedBy = new UserSummary(f.getModificationUser());
		this.cases = cases;
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
	
	public UserSummary getCreatedBy() {
		return createdBy;
	}
	
	public UserSummary getModifiedBy() {
		return modifiedBy;
	}

	public List<V_CaseSummary> getCases() {
		return cases;
	}
	
}
