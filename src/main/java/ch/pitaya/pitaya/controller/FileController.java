package ch.pitaya.pitaya.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.payload.response.FileDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.FileService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private Authorization auth;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileService fileService;

	@GetMapping("/{id}")
	public FileDetails getFileDetails(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<File> file_ = fileRepository.findByIdAndTheCaseFirm(id, firm);
		if (file_.isPresent()) {
			auth.require(file_.get(), AuthCode.FILE_READ);
			return new FileDetails(file_.get());
		}
		throw new ResourceNotFoundException("file", "id", id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> patchFileDetails(@PathVariable("id") Long id, @RequestBody PatchFileDetailsRequest request) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<File> file_ = fileRepository.findByIdAndTheCaseFirm(id, firm);
		if (file_.isPresent()) {
			fileService.patchFile(request, file_.get());
			return SimpleResponse.ok("Update successful");
		}
		throw new ResourceNotFoundException("file", "id", id);
	}

	@GetMapping("/{id}/content")
	public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
		Firm firm = securityFacade.getCurrentFirm();
		Optional<File> file_ = fileRepository.findByIdAndTheCaseFirm(id, firm);
		if (file_.isPresent()) {
			fileService.createFileDownload(response, file_.get());
		} else {
			throw new ResourceNotFoundException("file", "id", id);
		}
	}

	@PatchMapping("/{id}/content")
	@Authorize(AuthCode.FILE_EDIT)
	public ResponseEntity<?> editFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			File file = file_.get();
			fileService.addFileRevision(file, multipartFile);
			return SimpleResponse.ok("Update successful");
		}
		throw new BadRequestException("Invalid file id");
	}

	@DeleteMapping("/{id}/content")
	@Authorize(AuthCode.FILE_EDIT)
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			File file = file_.get();
			fileService.deleteFile(file);
			return SimpleResponse.ok("Deletion successful");
		}
		throw new BadRequestException("Invalid file id");
	}

}
