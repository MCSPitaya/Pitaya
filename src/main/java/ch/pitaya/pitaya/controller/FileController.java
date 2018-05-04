package ch.pitaya.pitaya.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.payload.response.FileDetails;

@RestController
@RequestMapping("/api/file")
public class FileController {
	
	@Autowired
	private FileRepository fileRepository;
	
	@GetMapping("/{id}")
	@Authorize(AuthCode.FILE_READ)
	public FileDetails getFileSummary(@PathVariable Long id) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			return new FileDetails(file_.get());
		}
		throw new ResourceNotFoundException("file", "id", id);
	}

}
