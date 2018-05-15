package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

	Optional<File> findById(Long id);

}
