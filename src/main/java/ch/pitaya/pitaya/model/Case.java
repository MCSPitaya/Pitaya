package ch.pitaya.pitaya.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Case {

	@Id
	@GeneratedValue
	private Long id;

	private String caseNumber;

	private String title;

	private String description;

	@ManyToOne
	private Firm firm;

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
	
}
