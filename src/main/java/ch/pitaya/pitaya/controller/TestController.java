package ch.pitaya.pitaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.TokenStore;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.repository.FirmRepository;
import ch.pitaya.pitaya.repository.TokenStoreRepository;
import ch.pitaya.pitaya.repository.UserRepository;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	FirmRepository firmRepo;

	@Autowired
	CaseRepository caseRepo;

	@Autowired
	FileRepository fileRepo;

	@Autowired
	TokenStoreRepository tokenRepo;

	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepo.findAll();
	}

	@GetMapping("/firms")
	public List<Firm> getFirms() {
		return firmRepo.findAll();
	}

	@GetMapping("/cases")
	public List<Case> getCases() {
		return caseRepo.findAll();
	}

	@GetMapping("/files")
	public List<File> getFiles() {
		return fileRepo.findAll();
	}

	@GetMapping("/tokens")
	public List<TokenStore> getTokens() {
		return tokenRepo.findAll();
	}

}
