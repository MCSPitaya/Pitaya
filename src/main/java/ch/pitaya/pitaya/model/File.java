package ch.pitaya.pitaya.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "files", uniqueConstraints = { //
		@UniqueConstraint(columnNames = { "case_id", "name" }) })
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "case_id", nullable = false, updatable = false)
	private Case theCase;

	@OrderBy("cre_dat DESC")
	@OneToMany(mappedBy = "file")
	private List<FileData> fileData;

	@OneToMany(mappedBy = "file")
	@OrderBy("cre_dat DESC")
	private List<Notification> notifications;

	@ElementCollection
	@MapKeyJoinColumn(name = "user_id")
	@Column(name = "auth_codes")
	@CollectionTable(name = "file_auth_codes", joinColumns = @JoinColumn(name = "file_id"))
	private Map<User, String> authCodes = new HashMap<>();

	@NotEmpty
	@Size(max = 80)
	@Column(nullable = false)
	private String name;

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

	protected File() {
		// JPA
	}

	public File(String name, Case theCase, User user) {
		this.theCase = theCase;
		this.name = name;
		this.cre_dat = new Timestamp(System.currentTimeMillis());
		this.mod_dat = this.cre_dat;
		cre_user = user;
		mod_user = user;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Case getCase() {
		return theCase;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCase(Case theCase) {
		this.theCase = theCase;
	}

	public List<FileData> getFileData() {
		return fileData;
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
