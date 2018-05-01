package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tokens")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;

	@NotBlank
	@Column(nullable = false, updatable = false)
	private String token;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp creationDate;

	@Column(name = "exp_dat", nullable = false, updatable = false)
	private Timestamp expirationDate;

	protected Token() {
		// JPA
	}

	public Token(User user, String token, Timestamp creationDate, Timestamp expirationDate) {
		super();
		this.token = token;
		this.user = user;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
	}

	public String getToken() {
		return token;
	}

	public Long getTokenId() {
		return tokenId;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public User getUser() {
		return user;
	}

}
