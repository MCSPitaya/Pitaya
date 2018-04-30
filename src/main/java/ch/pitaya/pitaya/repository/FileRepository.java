package ch.pitaya.pitaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;

public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findByTheCase(Case theCase);

	Optional<File> findByIdAndTheCaseFirm(Long id, Firm firm);

}
