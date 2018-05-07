package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Client;
import ch.pitaya.pitaya.model.Firm;

public interface ClientRepository extends JpaRepository<Client, Long>{
	
	Optional<Client> findByIdAndFirm(Long id, Firm firm);
	
}
