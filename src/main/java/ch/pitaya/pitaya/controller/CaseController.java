package ch.pitaya.pitaya.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.CaseDetails;
import ch.pitaya.pitaya.payload.CaseSummary;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.CaseService;

@RestController
@RequestMapping("/api/case")
public class CaseController {

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private CaseService caseService;

	@Autowired
	private CaseRepository caseRepository;

	@GetMapping
	public List<CaseSummary> getCaseList() {
		Firm firm = securityFacade.getCurrentFirm();
		List<Case> cases = caseService.getCaseList(firm);
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
}
