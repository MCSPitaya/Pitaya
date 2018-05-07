package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.model.Client;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.AddPatchClientRequest;
import ch.pitaya.pitaya.repository.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Authorize(AuthCode.FIRM_MANAGE_CLIENTS)
	public void patchClient(AddPatchClientRequest request, Client client) {
		if (request.getFirstName() != null) {
			client.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			client.setFirstName(request.getLastName());
		}
		if (request.getStreet() != null) {
			client.setFirstName(request.getStreet());
		}
		if (request.getNumber() != null) {
			client.setFirstName(request.getNumber());
		}
		if (request.getCity() != null) {
			client.setFirstName(request.getCity());
		}
		if (request.getZipCode() != null) {
			client.setFirstName(request.getZipCode());
		}
		if (request.getTelNumber() != null) {
			client.setFirstName(request.getTelNumber());
		}
		if (request.getEmail() != null) {
			client.setFirstName(request.getEmail());
		}
		clientRepository.save(client);
	}
	
	@Authorize(AuthCode.FIRM_MANAGE_CLIENTS)
	public void addClient(AddPatchClientRequest request, Firm firm) {
		Client client = new Client(firm, request.getFirstName(), request.getLastName(), request.getStreet(), request.getNumber(), request.getCity(), request.getZipCode(), request.getTelNumber(), request.getEmail());
		clientRepository.save(client);
	}

}
