package ch.pitaya.pitaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.model.Firm;

public interface FileDataRepository extends JpaRepository<FileData, Long> {

	List<FileData> findByFile(File file);

	Optional<FileData> findByIdAndFileTheCaseFirm(Long id, Firm firm);

}
