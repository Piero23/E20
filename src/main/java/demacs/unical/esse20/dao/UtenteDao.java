package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Utente;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteDao extends CrudRepository<Utente, UUID> {

    Optional<Utente> findbyUsername(String username);

    Optional<Utente> findbyEmail(String email);

}
