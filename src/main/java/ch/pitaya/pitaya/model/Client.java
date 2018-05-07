package ch.pitaya.pitaya.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	private String street;

	@NotBlank
	private String number;

	@NotBlank
	private String city;

	@NotBlank
	private String zipCode;

	private String telNumber;
	private String email;

	@ManyToOne(optional = false)
	@JoinColumn(name = "firm_id", updatable = false, nullable = false)
	private Firm firm;

	protected Client() {
		// JPA
	}

	public Client(Firm firm, String firstName, String lastName, String street, String number, String city,
			String zipCode, String telNumber, String email) {
		this.firm = firm;
		this.lastName = lastName;
		this.firstName = firstName;
		this.street = street;
		this.number = number;
		this.city = city;
		this.zipCode = zipCode;
		this.telNumber = telNumber;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Firm getFirm() {
		return firm;
	}

}
