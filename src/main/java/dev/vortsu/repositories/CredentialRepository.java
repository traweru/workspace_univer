package dev.barapp.repositories;

import dev.barapp.entities.CredentialEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CredentialRepository extends CrudRepository<CredentialEntity, Long> {
    @Query("SELECT c FROM CredentialEntity c WHERE c.email = :email")
    Optional<CredentialEntity> findCredentialEntityByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
