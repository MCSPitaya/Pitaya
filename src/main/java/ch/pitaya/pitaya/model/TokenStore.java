package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tokens")
public class TokenStore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;

	@NotBlank

	@Column(nullable = false)
	private String token;

	@Column(name = "exp_dat")
	private Timestamp expirationDate;

	protected TokenStore() {
		// JPA
	}

	public TokenStore(String token, Timestamp expirationDate) {
		super();
		this.token = token;
		this.expirationDate = expirationDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTokenId() {
		return tokenId;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

}
