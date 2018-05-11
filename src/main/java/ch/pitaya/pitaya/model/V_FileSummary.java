package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.pitaya.pitaya.authorization.AuthCodeProvider;

@Entity
@Table(name = "v_file_summary")
@Immutable
public class V_FileSummary implements AuthCodeProvider {

	@Id
	private Long id;

	@JsonIgnore
	private Long caseId;

	@JsonIgnore
	private Long userId;

	private String name;

	private Timestamp creDat, modDat;

	private int revisions;

	private String fileAuth, caseAuth, userAuth;

	protected V_FileSummary() {
		// JPA
	}

	public Long getId() {
		return id;
	}

	public Long getCaseId() {
		return caseId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public Timestamp getCreDat() {
		return creDat;
	}

	public Timestamp getModDat() {
		return modDat;
	}

	public int getRevisions() {
		return revisions;
	}

	@Override
	public String getFileAuthCodes() {
		return fileAuth;
	}

	@Override
	public String getCaseAuthCodes() {
		return caseAuth;
	}

	@Override
	public String getUserAuthCodes() {
		return userAuth;
	}

}
