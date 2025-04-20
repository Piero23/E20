package demacs.unical.esse20.service;

import demacs.unical.esse20.domain.Utente;

import java.util.List;
import java.util.Optional;

public interface UtenteService {

    // Crud Methods
    Utente save(Utente user);
    void delete(Utente user);

    // Search Methods
    List<Utente> getAllUsers();
    Optional<Utente> getUserByUsername(String username);
    Optional<Utente> getUserByEmail(String email);

    // External Usage Methods
}
