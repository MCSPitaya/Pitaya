package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;

public interface FileRepository extends JpaRepository<File, Long> {

	Optional<File> findByIdAndTheCaseFirm(Long id, Firm firm);

}
