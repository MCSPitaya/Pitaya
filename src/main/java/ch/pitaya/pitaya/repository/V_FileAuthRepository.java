package ch.pitaya.pitaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.pitaya.pitaya.model.V_FileAuth;

public interface V_FileAuthRepository extends JpaRepository<V_FileAuth, Long> {

	Optional<V_FileAuth> findByIdAndUserId(long fileId, long userId);

}
