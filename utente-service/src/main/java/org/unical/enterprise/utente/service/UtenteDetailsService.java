package org.unical.enterprise.utente.service;

import org.unical.enterprise.utente.data.dao.UtenteDAO;
import org.unical.enterprise.utente.data.model.Utente;
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
