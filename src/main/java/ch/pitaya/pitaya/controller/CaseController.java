package ch.pitaya.pitaya.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.payload.request.CreateCaseRequest;
import ch.pitaya.pitaya.payload.response.CaseDetails;
import ch.pitaya.pitaya.payload.response.CaseSummary;
import ch.pitaya.pitaya.payload.response.FileSummary;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileDataRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.FileService;
import ch.pitaya.pitaya.service.NotificationService;
import ch.pitaya.pitaya.util.Utils;

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
	private FileRepository fileRepository;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileDataRepository fileDataRepository;

	@GetMapping
	@Authorize(AuthCode.CASE_READ)
	public List<CaseSummary> getCaseList() {
		Firm firm = securityFacade.getCurrentFirm();
		List<Case> cases = firm.getCases();
		return Utils.map(cases, CaseSummary::new);
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
		notificationService.add(NotificationType.CASE_CREATED, case_);
		return new CaseDetails(case_);
	}

	@GetMapping("/{id}/files")
	@Authorize(AuthCode.CASE_READ_FILES)
	public List<FileSummary> getFileList(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent()) {
			List<File> files = case_.get().getFiles();
			return Utils.map(files, f -> new FileSummary(f.getId(), f.getName()));
		}
		throw new ResourceNotFoundException("case", "id", id);
	}
	
	@PostMapping("/{id}/file")
	@Authorize(AuthCode.FILE_CREATE)
	public ResponseEntity<?> addFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<Case> case_ = caseRepository.findByIdAndFirm(id, firm);
		if (case_.isPresent()) {
			File file = new File(multipartFile.getOriginalFilename(), (case_.get()));
			Optional<File> file_ = fileRepository.findByNameAndTheCaseId(file.getName(), case_.get().getId());
			if (file_.isPresent()) {
				throw new BadRequestException("File with that name already exists for that case");
			} else {
				file = fileRepository.save(file);
				try {
					FileData fileData = new FileData(file, fileService.createBlob(multipartFile.getInputStream(), multipartFile.getSize()));
					fileData = fileDataRepository.save(fileData);
					return SimpleResponse.ok("Update successful");
				} catch (IOException e) {
					throw new BadRequestException("Upload failed", e);
				}
			}
		}
		throw new BadRequestException("Invalid case id");
	}

}
