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

	@ManyToOne(targetEntity = Firm.class)
	private Firm firm;

	protected Case() {
		// JPA
	}

	public Case(String caseNumber, Firm firm) {
		this.caseNumber = caseNumber;
	}

	public void setCaseNumber(String caseNumber, Firm firm) {
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

}
