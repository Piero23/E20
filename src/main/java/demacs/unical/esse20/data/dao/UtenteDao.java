package demacs.unical.esse20.data.dao;

import demacs.unical.esse20.data.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteDao extends JpaRepository<Utente, UUID> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

}
