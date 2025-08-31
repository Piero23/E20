package org.unical.enterprise.utente.data.dao;

import org.unical.enterprise.utente.data.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteDAO extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByEmail(String email);
    Optional<Utente> findByUsername(String username);

    void deleteByUsername(String username);
}
