package ch.pitaya.pitaya.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@OneToMany(mappedBy = "file")
	private List<FileData> fileData;
	
	@OneToMany(mappedBy = "file")
	@OrderBy("cre_dat DESC")
	private List<Notification> notifications;

	@NotEmpty
	@Size(max = 80)
	@Column(nullable = false)
	private String name;

	protected File() {
		// JPA
	}

	public File(String name, Case theCase) {
		this.theCase = theCase;
		this.name = name;
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
	
}
