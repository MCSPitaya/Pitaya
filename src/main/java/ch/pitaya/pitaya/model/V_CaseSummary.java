package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.pitaya.pitaya.authorization.AuthCodeProvider;

@Entity
@Immutable
@Table(name = "v_case_summary")
public class V_CaseSummary implements AuthCodeProvider {

	@Id
	private Long id;

	private String caseNumber;

	private String title;

	private Timestamp cre_dat;

	private Timestamp mod_dat;

	@JsonIgnore
	private Long userId;

	@JsonIgnore
	private String case_auth;
	
	@JsonIgnore
	private String user_auth;

	protected V_CaseSummary() {
		// JPA
	}

	@Override
	public String getCaseAuthCodes() {
		return case_auth;
	}
	
	@Override
	public String getUserAuthCodes() {
		return user_auth;
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

	@Override
	public String getFileAuthCodes() {
		return null;
	}

}
