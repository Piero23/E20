package demacs.unical.esse20.service;

import demacs.unical.esse20.domain.AuthProvider;
import demacs.unical.esse20.domain.Utente;
import demacs.unical.esse20.dto.UtenteBasicDto;
import demacs.unical.esse20.dto.UtenteRegistrationDto;

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
    Utente registerNewUser(UtenteRegistrationDto userRegistrationDto);
    Utente registerNewUser(UtenteRegistrationDto userRegistrationDto, AuthProvider authProvider);

    // Parsing
    // UtenteBasicDto toUtenteBasicDto(Utente utente);
}
