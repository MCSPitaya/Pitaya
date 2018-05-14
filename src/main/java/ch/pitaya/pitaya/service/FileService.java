package ch.pitaya.pitaya.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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

import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.PatchFileDetailsRequest;
import ch.pitaya.pitaya.repository.FileDataRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.util.Utils;

@Service
public class FileService {

	private SessionFactory sessionFactory;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileDataRepository fileDataRepository;

	@Autowired
	private NotificationService notificationService;

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

	public void addFile(Case caze, MultipartFile multipartFile, User user) {
		File file = new File(multipartFile.getOriginalFilename(), caze, user);
		Optional<File> file_ = fileRepository.findByNameAndTheCaseId(file.getName(), caze.getId());
		if (file_.isPresent()) {
			throw new BadRequestException("File with that name already exists for this case");
		} else {
			file = fileRepository.save(file);
			try {
				FileData fileData = new FileData(file,
						createBlob(multipartFile.getInputStream(), multipartFile.getSize()), user);
				fileData = fileDataRepository.save(fileData);
				notificationService.add(NotificationType.FILE_CREATED, file);
			} catch (IOException e) {
				throw new BadRequestException("Upload failed", e);
			}
		}
	}

	public void patchFile(PatchFileDetailsRequest request, Long fileId, User user) {
		File file = fileRepository.findById(fileId).get();
		String oldName = file.getName();
		Utils.ifNotNull(request.getName(), file::setName);
		file.updateModification(user);
		fileRepository.save(file);
		notificationService.add(NotificationType.FILE_MODIFIED, file, oldName);
	}

	public void createFileDownload(HttpServletResponse response, File file) {
		response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
		List<FileData> fileDataList = file.getFileData();
		if (!fileDataList.isEmpty()) {
			FileData mostRecentFileData = fileDataList.get(0);
			try {
				IOUtils.copy(mostRecentFileData.getData().getBinaryStream(), response.getOutputStream());
			} catch (IOException | SQLException e) {
				throw new BadRequestException("Download failed", e);
			}
		} else {
			throw new BadRequestException("Requested deleted file");
		}
	}

	public void addFileRevision(File file, MultipartFile multipartFile, User user) {
		try {
			FileData fileData = new FileData(file, createBlob(multipartFile.getInputStream(), multipartFile.getSize()),
					user);
			fileData = fileDataRepository.save(fileData);
			file.updateModification(user);
			fileRepository.save(file);
			notificationService.add(NotificationType.FILE_VERSION_ADDED);
		} catch (IOException e) {
			throw new BadRequestException("Upload failed", e);
		}
	}

	public void deleteFile(File file, User user) {
		List<FileData> fileDataList = file.getFileData();
		System.out.println("List with files to delete: " + fileDataList.size());
		fileDataRepository.deleteAll(fileDataList);
		file.updateModification(user);
		fileRepository.save(file);
		notificationService.add(NotificationType.FILE_DELETED, file);
		System.out.println("Deleted");
	}
}
