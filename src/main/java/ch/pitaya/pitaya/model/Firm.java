package ch.pitaya.pitaya.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "firms")
public class Firm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String name;

	private String street;
	private String number;
	private String zipCode;
	private String city;

	@OneToMany(mappedBy = "firm")
	private List<User> users;

	@OneToMany(mappedBy = "firm")
	private List<Case> cases;
	
	@OneToMany(mappedBy = "firm")
	private List<Client> clients;

	@OneToMany(mappedBy = "firm")
	@OrderBy("cre_dat DESC")
	private List<Notification> notifications;
	
	

	protected Firm() {
		// JPA
	}

	public Firm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Case> getCases() {
		return cases;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}
	
	public List<Client> getClients() {
		return clients;
	}
	
}
