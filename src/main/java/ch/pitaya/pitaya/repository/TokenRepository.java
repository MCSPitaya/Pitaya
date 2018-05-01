package ch.pitaya.pitaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.pitaya.pitaya.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByToken(String token);

	@Query("SELECT t FROM Token t WHERE t.expirationDate < now()")
	List<Token> findExpiredTokens();

}
