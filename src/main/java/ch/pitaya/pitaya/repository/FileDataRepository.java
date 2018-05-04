package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;

@Transactional
public interface FileDataRepository extends JpaRepository<FileData, Long> {
	
	Optional<FileData> findByFile(File file);

}
