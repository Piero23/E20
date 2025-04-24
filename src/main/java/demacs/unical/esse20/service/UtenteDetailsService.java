package demacs.unical.esse20.service;

import demacs.unical.esse20.data.dao.UtenteDAO;
import demacs.unical.esse20.data.model.Utente;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteDetailsService implements UserDetailsService {
    private final UtenteDAO utenteDAO;

    public UtenteDetailsService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        Utente utente = utenteDAO.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail + " non trovato."));


        return new org.springframework.security.core.userdetails.User(
                utente.getEmail(),
                utente.getPassword(),
                List.of()
        );
    }

}
