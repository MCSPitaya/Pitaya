package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.FileData;
import ch.pitaya.pitaya.model.Firm;

public interface FileDataRepository extends JpaRepository<FileData, Long> {

	Optional<FileData> findByIdAndFileTheCaseFirm(Long id, Firm firm);

}
