package org.unical.enterprise.utente.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.utente.data.model.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UtenteDAO extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByEmail(String email);
    Optional<Utente> findByUsername(String username);

    Page<Utente> findByUsernameContaining(String username, Pageable pageable);

    void deleteByUsername(String username);
}
