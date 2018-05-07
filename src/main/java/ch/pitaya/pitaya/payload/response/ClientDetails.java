package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.Client;

public class ClientDetails {

	private long id;
	private String firstName, lastName, street, number, zipCode, city, tel, email;

	public ClientDetails(Client client) {
		id = client.getId();
		firstName = client.getFirstName();
		lastName = client.getLastName();
		street = client.getStreet();
		number = client.getNumber();
		zipCode = client.getZipCode();
		city = client.getCity();
		tel = client.getTelNumber();
		email = client.getEmail();
	}

	public long getId() {
		return id;
	}

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

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getTel() {
		return tel;
	}

	public String getEmail() {
		return email;
	}

}
