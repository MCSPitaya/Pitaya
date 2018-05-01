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
	
	@OneToMany(mappedBy="theCase")
	private List<File> files;

	protected Case() {
		// JPA
	}

	public Case(Firm firm, String caseNumber, String title, String description) {
		this.caseNumber = caseNumber;
		this.title = title;
		this.description = description;
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

}
