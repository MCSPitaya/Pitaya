package ch.pitaya.pitaya.payload.request;

import javax.validation.constraints.NotBlank;

public class AddPatchClientRequest {
	
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
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
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
