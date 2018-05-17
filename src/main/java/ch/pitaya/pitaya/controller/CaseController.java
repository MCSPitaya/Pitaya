package ch.pitaya.pitaya.controller;

import java.util.stream.Collectors;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.model.V_FileSummary;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.CourtRepository;
import ch.pitaya.pitaya.repository.V_CaseSummaryRepository;
import ch.pitaya.pitaya.repository.V_FileSummaryRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.CaseService;
import ch.pitaya.pitaya.service.FileService;
import ch.pitaya.pitaya.service.NotificationService;
import ch.pitaya.pitaya.service.SSEService;

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
	private V_CaseSummaryRepository caseSummaryRepo;

	@Autowired
	private V_FileSummaryRepository fileSummaryRepo;

	@Autowired
	private Authorization auth;

	@Autowired
	private SSEService sse;

	@GetMapping
	@Transactional
	public Object getCaseList() {
		long userId = securityFacade.getCurrentUserId();

		return caseSummaryRepo.findAllByUserId(userId).filter(c -> auth.test(c, AuthCode.CASE_READ))
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@AuthorizeCase(AuthCode.CASE_READ)
	public CaseDetails getCaseDetails(@PathVariable Long id) {
		return new CaseDetails(caseRepository.findById(id).get());
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
		sse.emit(case_.getFirm().getId(), "cases", "update", "case created");
		return new CaseDetails(case_);
	}

	@GetMapping("/{id}/files")
	@AuthorizeCase(AuthCode.CASE_READ)
	public Stream<V_FileSummary> getFileList(@PathVariable Long id) {
		Long userId = securityFacade.getCurrentUserId();
		return fileSummaryRepo.findByCaseIdAndUserId(id, userId).stream().filter(f -> auth.test(f, AuthCode.FILE_READ));
	}

	@PatchMapping("/{id}")
	@AuthorizeCase(AuthCode.CASE_MODIFY)
	public Object patchCase(@PathVariable("id") Long id) {
		Case case_ = caseRepository.findById(id).get();
		caseService.patchCase(case_);
		sse.emit(case_.getFirm().getId(), "cases", "update", "case updated");
		return SimpleResponse.ok("case updated");
	}

	@PostMapping("/{id}/file")
	@AuthorizeCase(AuthCode.CASE_CREATE_FILE)
	public ResponseEntity<?> addFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		User user = securityFacade.getCurrentUser();
		Case case_ = caseRepository.findById(id).get();
		fileService.addFile(case_, multipartFile, user);
		return SimpleResponse.ok("Update successful");
	}

	// SSE ENDPOINTS
	@GetMapping("/events")
	public SseEmitter getCaseSSE() {
		Long firmId = securityFacade.getCurrentFirmId();
		return sse.create(firmId, "cases");
	}

}
