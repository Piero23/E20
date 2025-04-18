package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteDao extends JpaRepository<Utente, Long> {

    Optional<Utente> findbyUsername(String username);

    Optional<Utente> findbyEmail(String email);

}
