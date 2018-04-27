package ch.pitaya.pitaya.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tokenStore", uniqueConstraints = { @UniqueConstraint(columnNames = "token") })
public class TokenStore {

	@Id
	@GeneratedValue
	private Long tokenId;

	private String token;

	protected TokenStore() {
		// JPA
	}

	public TokenStore(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
