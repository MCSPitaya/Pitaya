package ch.pitaya.pitaya.controller;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.CaseSummary;
import ch.pitaya.pitaya.payload.response.FileSummary;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.NotificationService;

@RestController
@RequestMapping("/api/case")
public class CaseController {

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private Authorization auth;

	@GetMapping
	@Authorize(AuthCode.CASE_READ)
	public Stream<CaseSummary> getCaseList() {
		Firm firm = securityFacade.getCurrentFirm();
		return firm.getCases().stream().filter(c -> auth.test(c, AuthCode.CASE_READ)).map(CaseSummary::new);
	}

	@GetMapping("/{id}")
	public CaseDetails getCaseDetails(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm).filter(c -> auth.test(c, AuthCode.CASE_READ));
		if (case_.isPresent())
			return new CaseDetails(case_.get());
		throw new ResourceNotFoundException("case", "id", id);
	}

	@PostMapping
	@Transactional
	@Authorize(AuthCode.CASE_CREATE)
	public CaseDetails createCase(@Valid @RequestBody CreateCaseRequest request) {
		Firm firm = securityFacade.getCurrentFirm();
		Case case_ = caseRepository
				.save(new Case(firm, request.getNumber(), request.getTitle(), request.getDescription()));
		notificationService.add(NotificationType.CASE_CREATED, case_);
		return new CaseDetails(case_);
	}

	@GetMapping("/{id}/files")
	@Authorize(AuthCode.CASE_READ_FILES)
	public Stream<FileSummary> getFileList(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent())
			return case_.get().getFiles().stream().filter(f -> auth.test(f, AuthCode.FILE_READ)).map(FileSummary::new);
		throw new ResourceNotFoundException("case", "id", id);
	}

}
