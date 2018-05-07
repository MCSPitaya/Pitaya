package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Immutable
@Table(name = "v_case_summary")
public class V_CaseSummary {

	@Id
	private Long id;

	private String caseNumber;

	private String title;

	private Timestamp cre_dat;

	private Timestamp mod_dat;

	@JsonIgnore
	private Long userId;

	@JsonIgnore
	private String authCodes;

	protected V_CaseSummary() {
		// JPA
	}

	public String getAuthCodes() {
		return authCodes;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public Timestamp getCre_dat() {
		return cre_dat;
	}

	public Long getId() {
		return id;
	}

	public Timestamp getMod_dat() {
		return mod_dat;
	}

	public String getTitle() {
		return title;
	}

	public Long getUserId() {
		return userId;
	}

}
