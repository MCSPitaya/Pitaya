package ch.pitaya.pitaya.controller;

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
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.User;
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
	private FileRepository fileRepository;

	@Autowired
	private FileService fileService;

	@GetMapping("/{id}")
	@AuthorizeFile(AuthCode.FILE_READ)
	public FileDetails getFileDetails(@PathVariable Long id) {
		File file = fileRepository.findById(id).get();
		return new FileDetails(file);
	}

	@PatchMapping("/{id}")
	@AuthorizeFile(AuthCode.FILE_EDIT)
	public ResponseEntity<?> patchFileDetails(@PathVariable("id") Long id,
			@RequestBody PatchFileDetailsRequest request) {
		User user = securityFacade.getCurrentUser();
		File file = fileRepository.findById(id).get();
		fileService.patchFile(request, file, user);
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

}
