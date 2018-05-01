package ch.pitaya.pitaya.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@NotBlank
	@Size(max = 40)
	@Column(nullable = false)
	private String name;

	@NotBlank
	@Size(max = 15)
	@Column(nullable = false, unique = true, updatable = false)
	private String username;

	@NaturalId
	@NotBlank
	@Size(max = 40)
	@Email
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false)
	private String password;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Firm firm;

	@OneToMany(mappedBy = "user")
	private List<Token> tokens;

	@OneToMany(mappedBy = "user")
	private List<Notification> notifications;

	@NotBlank
	@Column(nullable = false)
	private String authCodes;

	public User() {

	}

	public User(String name, String username, String email, String password, Firm firm, String authCodes) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.firm = firm;
		this.authCodes = authCodes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	public String getAuthCodes() {
		return authCodes;
	}

	public void setAuthCodes(String authCodes) {
		this.authCodes = authCodes;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

}