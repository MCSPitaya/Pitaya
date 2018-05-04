package ch.pitaya.pitaya.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cases")
public class Case {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@NotBlank
	private String caseNumber;

	@NotBlank
	@Column(nullable = false)
	private String title;

	private String description;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Firm firm;

	@OneToMany(mappedBy = "theCase")
	private List<File> files;

	@OneToMany(mappedBy = "theCase")
	@OrderBy("cre_dat DESC")
	private List<Notification> notifications;

	@ElementCollection
	@MapKeyJoinColumn(name = "user_id")
	@Column(name = "auth_codes")
	@CollectionTable(name = "case_auth_codes", joinColumns = @JoinColumn(name = "case_id"))
	private Map<User, String> authCodes = new HashMap<>();

	protected Case() {
		// JPA
	}

	public Case(Firm firm, String caseNumber, String title, String description) {
		this.caseNumber = caseNumber;
		this.title = title;
		this.description = description;
		this.firm = firm;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<File> getFiles() {
		return files;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public Map<User, String> getAuthCodes() {
		return authCodes;
	}

	public String getAuthCodes(User user) {
		return authCodes.get(user);
	}

	public void setAuthCodes(User user, String auth_codes) {
		authCodes.put(user, auth_codes);
	}

}
