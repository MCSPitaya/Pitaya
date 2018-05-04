package ch.pitaya.pitaya.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.repository.FileDataRepository;
import ch.pitaya.pitaya.repository.FileRepository;

@Service
public class FileService {

	private SessionFactory sessionFactory;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileDataRepository fileDataRepository;

	@Autowired
	public FileService(EntityManagerFactory factory) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	public Blob createBlob(InputStream content, long size) {
		try {
			sessionFactory.getCurrentSession();
			return (Blob) sessionFactory.getCurrentSession().getLobHelper().createBlob(content, size);
		} catch (HibernateException e) {
			sessionFactory.openSession();
			return (Blob) sessionFactory.openSession().getLobHelper().createBlob(content, size);
		}
	}

	@AuthorizeCase(AuthCode.CASE_CREATE_FILE)
	public void addFile(Case caze, MultipartFile multipartFile) {
		File file = new File(multipartFile.getOriginalFilename(), (caze));
		Optional<File> file_ = fileRepository.findByNameAndTheCaseId(file.getName(), caze.getId());
		if (file_.isPresent()) {
			throw new BadRequestException("File with that name already exists for this case");
		} else {
			file = fileRepository.save(file);
			try {
				FileData fileData = new FileData(file,
						createBlob(multipartFile.getInputStream(), multipartFile.getSize()));
				fileData = fileDataRepository.save(fileData);
			} catch (IOException e) {
				throw new BadRequestException("Upload failed", e);
			}
		}
	}

	@AuthorizeFile(AuthCode.FILE_EDIT)
	public void patchFile(PatchFileDetailsRequest request, File file) {
		if (request.getName() != null) {
			Optional<File> file__ = fileRepository.findByNameAndTheCaseId(request.getName(), file.getCase().getId());
			if (file__.isPresent()) {
				throw new BadRequestException("File with that name already exists for this case");
			} else {
				file.setName(request.getName());
			}
		}
		fileRepository.save(file);
	}

	@AuthorizeFile(AuthCode.FILE_READ)
	public void createFileDownload(HttpServletResponse response, File file) {
		response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
		List<FileData> fileDataList = file.getFileData();
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
	}

	@AuthorizeFile(AuthCode.FILE_UPDATE)
	public void addFileRevision(File file, MultipartFile multipartFile) {
		try {
			FileData fileData = new FileData(file, createBlob(multipartFile.getInputStream(), multipartFile.getSize()));
			fileData = fileDataRepository.save(fileData);
		} catch (IOException e) {
			throw new BadRequestException("Upload failed", e);
		}
	}
	
	@AuthorizeFile(AuthCode.FILE_DELETE)
	public void deleteFile(File file) {
		List<FileData> fileDataList = file.getFileData();
		System.out.println("List with files to delete: " + fileDataList.size());
		fileDataRepository.deleteAll(fileDataList);
		System.out.println("Deleted");
	}
}
