package ch.pitaya.pitaya.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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

import ch.pitaya.pitaya.repository.FileDataRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.service.FileService;
import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.payload.response.FileDetails;
import ch.pitaya.pitaya.payload.response.SimpleResponse;

@RestController
@RequestMapping("/api/file")
public class FileController {
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private FileDataRepository fileDataRepository;
	
	@Autowired
	private FileService fileService;
	
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
	
	@GetMapping("/{id}/content")
	@Authorize(AuthCode.FILE_READ)
	public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			response.addHeader("Content-Disposition", "attachment; filename=" + file_.get().getName());
			List<FileData> fileDataList = file_.get().getFileData();
			if (!fileDataList.isEmpty()) {
				FileData mostRecentFileData = Collections.max(fileDataList, Comparator.comparingLong(FileData::getId));
				try {
					IOUtils.copy(mostRecentFileData.getData().getBinaryStream(), response.getOutputStream());
				} catch (IOException | SQLException e) {
					throw new BadRequestException("Download failed", e);
				}
			} else {
				throw new BadRequestException("Requested deleted file");
			}
		} else {
			throw new ResourceNotFoundException("file", "id", id);
		}
	}
	
	@PatchMapping("/{id}/content")
	@Authorize(AuthCode.FILE_EDIT)
	public ResponseEntity<?> editFile(@PathVariable Long id, @RequestPart("file") MultipartFile multipartFile) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			try {
				FileData fileData = new FileData(file_.get(), fileService.createBlob(multipartFile.getInputStream(), multipartFile.getSize()));
				fileData = fileDataRepository.save(fileData);
				return SimpleResponse.ok("Update successful");
			} catch (IOException e) {
				throw new BadRequestException("Upload failed", e);
			}
		}
		throw new BadRequestException("Invalid file id");
	}
	
	@DeleteMapping("/{id}/content")
	@Authorize(AuthCode.FILE_EDIT)
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {
		Optional<File> file_ = fileRepository.findById(id);
		if (file_.isPresent()) {
			List<FileData> fileDataList = file_.get().getFileData();
			System.out.println("List with files to delete: " + fileDataList.size());
			fileDataRepository.deleteAll(fileDataList);
			System.out.println("Deleted");
			return SimpleResponse.ok("Deletion successful");
		}
		throw new BadRequestException("Invalid file id");
	}

}
