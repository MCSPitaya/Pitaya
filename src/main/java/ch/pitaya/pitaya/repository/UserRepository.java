package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<User> findByIdAndFirm(Long id, Firm firm);

}
