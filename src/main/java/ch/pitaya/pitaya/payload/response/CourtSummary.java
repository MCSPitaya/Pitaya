package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.Court;

public class CourtSummary {
	
	private Long id;
	private String name;
	private String street;
	private String number;
	private String city;
	private String zipCode;
	private String telNumber;
	private String email;
	
	public CourtSummary(Court c) {
		this.id = c.getId();
		this.name = c.getName();
		this.street = c.getStreet();
		this.number = c.getNumber();
		this.city = c.getCity();
		this.zipCode = c.getZipCode();
		this.telNumber = c.getTelNumber();
		this.email = c.getEmail();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public String getTelNumber() {
		return telNumber;
	}
	
	public String getEmail() {
		return email;
	}	

}
