package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.PatchFirmRequest;
import ch.pitaya.pitaya.repository.FirmRepository;

@Service
public class FirmService {
	
	@Autowired
	private FirmRepository firmRepository;
	
	public void editFirm(PatchFirmRequest request, Firm firm) {
		if (request.getName() != null) {
			firm.setName(request.getName());
		}
		if (request.getStreet() != null) {
			firm.setStreet(request.getStreet());
		}
		if (request.getNumber() != null) {
			firm.setNumber(request.getNumber());
		}
		if (request.getCity() != null) {
			firm.setCity(request.getCity());
		}
		if (request.getZipCode() != null) {
			firm.setZipCode(request.getZipCode());
		}
		firmRepository.save(firm);
	}

}
