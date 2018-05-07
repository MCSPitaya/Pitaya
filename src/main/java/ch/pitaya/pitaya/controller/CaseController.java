package ch.pitaya.pitaya.controller;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.CaseSummary;
import ch.pitaya.pitaya.payload.response.FileSummary;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.CourtRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.CaseService;
import ch.pitaya.pitaya.service.FileService;
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
	private FileService fileService;

	@Autowired
	private CaseService caseService;

	@Autowired
	private CourtRepository courtRepo;

	@Autowired
	private Authorization auth;

	@GetMapping
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
	@Authorize(AuthCode.FIRM_CASE_CREATE)
	public CaseDetails createCase(@Valid @RequestBody CreateCaseRequest request) {
		User user = securityFacade.getCurrentUser();
		Firm firm = user.getFirm();

		Case case_ = courtRepo.findById(request.getCourtId())
				.map(c -> new Case(firm, c, request.getNumber(), request.getTitle(), request.getDescription(), user))
				.orElseThrow(() -> new ResourceNotFoundException("court", "id", request.getCourtId()));
		case_ = caseRepository.save(case_);
		notificationService.add(NotificationType.CASE_CREATED, case_);
		return new CaseDetails(case_);
	}

	@GetMapping("/{id}/files")
	public Stream<FileSummary> getFileList(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		return caseRepository.findByIdAndFirm(id, firm).map(caseService::getFileList)
				.orElseThrow(() -> new ResourceNotFoundException("case", "id", id));
	}

	@PatchMapping("/{id}")
	public Object patchCase(@PathVariable("id") Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent()) {
			caseService.patchCase(case_.get());
			return SimpleResponse.ok("case updated");
		}
		throw new ResourceNotFoundException("case", "id", id);
	}

	@PostMapping("/{id}/file")
	public ResponseEntity<?> addFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		User user = securityFacade.getCurrentUser();
		Firm firm = user.getFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent()) {
			fileService.addFile(case_.get(), multipartFile, user);
			return SimpleResponse.ok("Update successful");
		}
		throw new ResourceNotFoundException("case", "id", id);
	}

}
