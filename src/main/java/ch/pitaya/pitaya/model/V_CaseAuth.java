package ch.pitaya.pitaya.model;

import javax.annotation.concurrent.Immutable;
import javax.persistence.*;

import ch.pitaya.pitaya.authorization.AuthCodeProvider;

@Entity
@Immutable
@Table(name = "v_case_auth")
public class V_CaseAuth implements AuthCodeProvider {

	@Id
	private Long id;

	private Long userId;

	private String caseAuth;

	private String userAuth;

	protected V_CaseAuth() {
		// JPA
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String getFileAuthCodes() {
		return null;
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
