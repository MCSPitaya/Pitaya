package ch.pitaya.pitaya.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.payload.response.FileDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;

@RestController
@RequestMapping("/api/file")
public class FileController {
	
	@Autowired
	private FileRepository fileRepository;
	
	@GetMapping("/{id}")
	@Authorize(AuthCode.FILE_READ)
	public FileDetails getFileDetails(@PathVariable Long id) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			return new FileDetails(file_.get());
		}
		throw new ResourceNotFoundException("file", "id", id);
	}
	
	@PatchMapping("/{id}")
	@Authorize(AuthCode.FILE_EDIT)
	public ResponseEntity<?> patchFileDetails(@RequestBody PatchFileDetailsRequest request) {
		Optional<File> file_ = fileRepository.findById(request.getId());
		if (file_.isPresent()) {
			if (request.getName() != null ) {
				Optional<File> file__ = fileRepository.findByNameAndTheCaseId(request.getName(), file_.get().getCase().getId());
				if (file__.isPresent()) {
					throw new BadRequestException("File with that name already exists for this case");
				} else {
					file_.get().setName(request.getName());
				}
			}
			fileRepository.save(file_.get());
			return SimpleResponse.ok("Update successful");
		}
		throw new ResourceNotFoundException("file", "id", request.getId());
	} 

}
