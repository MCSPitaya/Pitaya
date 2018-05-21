package ch.pitaya.pitaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.RequestLogEntry;

public interface RequestLogEntryRepository extends JpaRepository<RequestLogEntry, Long> {

}
