package ch.pitaya.pitaya.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.model.V_CaseSummary;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.payload.response.FileDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.repository.V_CaseSummaryRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.FileService;
import ch.pitaya.pitaya.service.NotificationService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private V_CaseSummaryRepository caseSummaryRepo;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private Authorization auth;

	@Autowired
	private FileService fileService;

	@GetMapping("/{id}")
	@AuthorizeFile(AuthCode.FILE_READ)
	@Transactional
	public FileDetails getFileDetails(@PathVariable Long id) {
		File file = fileRepository.findById(id).get();
		Long userId = securityFacade.getCurrentUserId();
		List<V_CaseSummary> cases = file.getCases().stream()
				.map(c -> caseSummaryRepo.findByIdAndUserId(c.getId(), userId)).filter(Optional::isPresent)
				.map(Optional::get).filter(c -> auth.test(c, AuthCode.CASE_READ)).collect(Collectors.toList());
		return new FileDetails(file, cases);
	}

	@PatchMapping("/{id}")
	@AuthorizeFile(AuthCode.FILE_EDIT)
	public ResponseEntity<?> patchFileDetails(@PathVariable("id") Long id,
			@RequestBody PatchFileDetailsRequest request) {
		User user = securityFacade.getCurrentUser();
		fileService.patchFile(request, id, user);
		return SimpleResponse.ok("Update successful");
	}

	@GetMapping("/{id}/content")
	@AuthorizeFile(AuthCode.FILE_READ)
	public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
		File file = fileRepository.findById(id).get();
		fileService.createFileDownload(response, file);
	}

	@PatchMapping("/{id}/content")
	@AuthorizeFile(AuthCode.FILE_UPDATE)
	public ResponseEntity<?> editFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		User user = securityFacade.getCurrentUser();
		File file = fileRepository.findById(id).get();
		fileService.addFileRevision(file, multipartFile, user);
		return SimpleResponse.ok("Update successful");
	}

	@DeleteMapping("/{id}")
	@AuthorizeFile(AuthCode.FILE_DELETE)
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {
		User user = securityFacade.getCurrentUser();
		File file = fileRepository.findById(id).get();
		fileService.deleteFile(file, user);
		return SimpleResponse.ok("Deletion successful");
	}

	@PostMapping("/{fid}/addToCase/{cid}")
	@AuthorizeFile(value = AuthCode.FILE_EDIT, param = "fid")
	@AuthorizeCase(value = AuthCode.CASE_CREATE_FILE, param = "cid")
	@Transactional
	public ResponseEntity<?> addToAnotherCase(@PathVariable Long fid, @PathVariable Long cid) {
		File f = fileRepository.findById(fid).get();
		Case c = caseRepository.findById(cid).get();
		List<Case> caseList = f.getCases();
		if (caseList.contains(c))
			throw new BadRequestException("file " + fid + " already in case " + cid);
		caseList.add(c);
		f.updateModification(securityFacade.getCurrentUser());
		fileRepository.save(f);
		notificationService.add(NotificationType.FILE_ADDED_TO_CASE, f);
		return SimpleResponse.ok("file added to case");
	}

}
