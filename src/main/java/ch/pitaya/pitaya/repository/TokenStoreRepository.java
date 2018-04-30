package ch.pitaya.pitaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.pitaya.pitaya.model.TokenStore;

public interface TokenStoreRepository extends JpaRepository<TokenStore, Long> {

	Optional<TokenStore> findByToken(String token);

	@Query("SELECT t FROM TokenStore t WHERE t.expirationDate < now()")
	List<TokenStore> findExpiredTokens();

}
