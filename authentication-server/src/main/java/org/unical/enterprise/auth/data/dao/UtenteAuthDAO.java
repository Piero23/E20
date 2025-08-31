package org.unical.enterprise.auth.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.auth.data.model.UtenteAuth;

import java.util.Optional;
import java.util.UUID;

public interface UtenteAuthDAO extends JpaRepository<UtenteAuth, UUID> {

    Optional<UtenteAuth> findByUsername(String username);

    boolean existsByUsername(String username);
}
