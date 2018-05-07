package ch.pitaya.pitaya.controller;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Client;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.AddPatchClientRequest;
import ch.pitaya.pitaya.payload.response.ClientDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.ClientRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.ClientService;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
	private SecurityFacade securityFacade;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientService clientService;
	
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
	
	@PostMapping
	public ResponseEntity<?> addClient(@Valid @RequestBody AddPatchClientRequest request) {
		clientService.addClient(request, securityFacade.getCurrentFirm());
		return SimpleResponse.ok("Successfully created client");
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> editClient(@RequestBody AddPatchClientRequest request, @PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Client> client_ = clientRepository.findByIdAndFirm(id, firm);
		if (client_.isPresent()) {
			clientService.patchClient(request, client_.get());
			return SimpleResponse.ok("Update successful");
		}
		throw new ResourceNotFoundException("client", "id", id);
	}

}
