package ch.pitaya.pitaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findByTheCase(Case theCase);

}
