package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.Firm;

public class FirmSummary {
	
	private Long id;
	private String name;
	private String street;
	private String number;
	private String zipCode;
	private String city;
	private int userCount;
	
	public FirmSummary(Long id, String name, String street, String number, String zipCode, String city) {
		this.id = id;
		this.name = name;
		this.street = street;
		this.number = number;
		this.zipCode = zipCode;
		this.city = city;
	}
	
	public FirmSummary(Firm firm) {
		this.id = firm.getId();
		this.name = firm.getName();
		this.street = firm.getStreet();
		this.number = firm.getNumber();
		this.zipCode = firm.getZipCode();
		this.city = firm.getCity();
		this.userCount = firm.getUsers().size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getUserCount() {
		return userCount;
	}
	
}
