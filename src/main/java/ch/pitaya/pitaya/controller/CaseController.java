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

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.CaseSummary;
import ch.pitaya.pitaya.payload.response.FileSummary;
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
	@Authorize(AuthCode.CASE_READ)
	public List<CaseSummary> getCaseList() {
		Firm firm = securityFacade.getCurrentFirm();
		List<Case> cases = firm.getCases();
		List<CaseSummary> response = new ArrayList<>(cases.size());
		for (Case c : cases)
			response.add(new CaseSummary(c));
		return response;
	}

	@GetMapping("/{id}")
	@Authorize(AuthCode.CASE_READ)
	public CaseDetails getCaseDetails(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent())
			return new CaseDetails(case_.get());
		throw new ResourceNotFoundException("case", "id", id);
	}

	@PostMapping
	@Authorize(AuthCode.CASE_CREATE)
	public CaseDetails createCase(@Valid @RequestBody CreateCaseRequest request) {
		Firm firm = securityFacade.getCurrentFirm();
		Case case_ = caseRepository
				.save(new Case(firm, request.getNumber(), request.getTitle(), request.getDescription()));
		return new CaseDetails(case_);
	}

	@GetMapping("/{id}/files")
	@Authorize(AuthCode.CASE_READ_FILES)
	public List<FileSummary> getFileList(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent()) {
			List<File> files = case_.get().getFiles();
			List<FileSummary> summaries = new ArrayList<>();
			for (File f : files) {
				summaries.add(new FileSummary(f.getId(), f.getName()));
			}
			return summaries;
		}
		throw new ResourceNotFoundException("case", "id", id);
	}

}
