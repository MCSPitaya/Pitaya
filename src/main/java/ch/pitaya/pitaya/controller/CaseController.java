package ch.pitaya.pitaya.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.CaseSummary;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.security.SecurityFacade;

@RestController
@RequestMapping("/api/case")
public class CaseController {

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private CaseRepository caseRepository;

	@GetMapping
	public List<CaseSummary> getCaseList() {
		Firm firm = securityFacade.getCurrentFirm();
		List<Case> cases = caseRepository.findByFirm(firm);
		List<CaseSummary> response = new ArrayList<>(cases.size());
		for (Case c : cases)
			response.add(new CaseSummary(c));
		return response;
	}

	@GetMapping("/{id}")
	public CaseDetails getCaseDetails(@PathVariable Long id) {
		Optional<Case> case_ = caseRepository.findById(id);
		if (case_.isPresent())
			return new CaseDetails(case_.get());
		throw new ResourceNotFoundException("case", "id", id);
	}

	@PostMapping
	public CaseDetails createCase(@Valid @RequestBody CreateCaseRequest request) {
		Firm firm = securityFacade.getCurrentFirm();
		Case case_ = caseRepository
				.save(new Case(firm, request.getNumber(), request.getTitle(), request.getDescription()));
		return new CaseDetails(case_);
	}

}
