package ch.pitaya.pitaya.controller;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Client;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.response.ClientDetails;
import ch.pitaya.pitaya.repository.ClientRepository;
import ch.pitaya.pitaya.security.SecurityFacade;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
	private SecurityFacade securityFacade;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@GetMapping
	public Stream<ClientDetails> getClientList() {
		Firm firm = securityFacade.getCurrentFirm();
		return firm.getClients().stream().map(ClientDetails::new);
	}
	
	@GetMapping("/{id}")
	public ClientDetails getClient(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Client> client_ = clientRepository.findByIdAndFirm(id, firm);
		if (client_.isPresent()) {
			return new ClientDetails(client_.get());
		}
		throw new ResourceNotFoundException("client", "id", id);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> addClient(@PathVariable Long id) {
		
		throw new ResourceNotFoundException("client", "id", id);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> editClient(@PathVariable Long id) {
		
		throw new ResourceNotFoundException("client", "id", id);
	}

}
