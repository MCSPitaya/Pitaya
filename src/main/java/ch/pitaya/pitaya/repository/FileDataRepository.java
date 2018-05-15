package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ch.pitaya.pitaya.model.FileData;

@Transactional
public interface FileDataRepository extends JpaRepository<FileData, Long> {

}
