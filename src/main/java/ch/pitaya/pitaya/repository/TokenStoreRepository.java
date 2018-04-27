package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.TokenStore;

public interface TokenStoreRepository extends JpaRepository<TokenStore, Long> {

	Optional<TokenStore> findByToken(String token);

}
