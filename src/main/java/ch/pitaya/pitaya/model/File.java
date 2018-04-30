package ch.pitaya.pitaya.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class File {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Case theCase;

	@NotEmpty
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

}
