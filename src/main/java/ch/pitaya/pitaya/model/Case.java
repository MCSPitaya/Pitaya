package ch.pitaya.pitaya.model;

import java.sql.Timestamp;
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

	@Column(nullable = false, updatable = false)
	private Timestamp cre_dat;

	@Column(nullable = false, updatable = false)
	private Timestamp mod_dat;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "cre_uid", nullable = false, updatable = false)
	private User cre_user;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "mod_uid", nullable = false, updatable = true)
	private User mod_user;


	protected Case() {
		// JPA
	}

	public Case(Firm firm, String caseNumber, String title, String description, User user) {
		this.caseNumber = caseNumber;
		this.title = title;
		this.description = description;
		this.firm = firm;
		this.cre_dat = new Timestamp(System.currentTimeMillis());
		this.mod_dat = this.cre_dat;
		cre_user = user;
		mod_user = user;
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

	public Timestamp getCreationTime() {
		return cre_dat;
	}

	public Timestamp getModificationTime() {
		return mod_dat;
	}
	
	public void setModificationTime(Timestamp mod_dat) {
		this.mod_dat = mod_dat;
	}
	
	public void setMod_user(User mod_user) {
		this.mod_user = mod_user;
	}
	
	public void updateModification(User user) {
		this.mod_dat = new Timestamp(System.currentTimeMillis());
		this.mod_user = user;
	}

	public User getCreationUser() {
		return cre_user;
	}

	public User getModificationUser() {
		return mod_user;
	}

}
