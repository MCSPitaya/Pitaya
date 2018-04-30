package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.FileData;

public interface FileDataRepository extends JpaRepository<FileData, Long> {

}
