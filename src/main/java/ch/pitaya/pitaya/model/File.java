package ch.pitaya.pitaya.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

public class File {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(targetEntity = Case.class)
	private Case owner;

	@NotEmpty
	private String name;

	protected File() {
		// JPA
	}

	public File(String name, Case owner) {
		this.owner = owner;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Case getOwner() {
		return owner;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(Case owner) {
		this.owner = owner;
	}

}
