package org.unical.enterprise.auth.service;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.auth.data.dao.UtenteAuthDAO;
import org.unical.enterprise.auth.data.dto.UtenteRegistrationDTO;
import org.unical.enterprise.auth.data.model.Role;
import org.unical.enterprise.auth.data.model.UtenteAuth;
import org.unical.enterprise.auth.exceptions.UsernameAlreadyExistsException;
import org.unical.enterprise.auth.exceptions.UserProfileCreationException;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private UtenteAuthDAO utenteAuthDAO;
    private UtenteServiceClient utenteServiceClient;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UtenteAuth registerNewUser(@Valid UtenteRegistrationDTO utenteRegistrationDTO) {

        // Controlla se lo username è gia presente
        if (utenteAuthDAO.existsByUsername(utenteRegistrationDTO.getUsername()))
            throw new UsernameAlreadyExistsException(
                    String.format("Username \"%s\" già in uso", utenteRegistrationDTO.getUsername())
            );


        // Registra l'Utente lato Authentication-Server
        UtenteAuth newUser = utenteAuthDAO.save(UtenteAuth.builder()
                        .id(UUID.randomUUID())
                        .username(utenteRegistrationDTO.getUsername())
                        .password(passwordEncoder.encode(utenteRegistrationDTO.getPassword()))
                        .roles(Set.of(Role.USER))
                        .build());

        try {
            // Registra l'Utente lato Utente-Service
            utenteServiceClient.register(UtenteDTO.builder()
                        .id(newUser.getId())
                        .username(newUser.getUsername())
                        .email(utenteRegistrationDTO.getEmail())
                        .dataNascita(utenteRegistrationDTO.getDataNascita())
                        .build());
        }
        catch (Exception e) {
            // L'eccezione fa rollback anche sul DB locale
            throw new UserProfileCreationException("Registrazione Fallita su Utente-Service", e);
        }

        return newUser;
    }

    @Transactional
    public void deleteUser(String username) {
        UtenteAuth user = utenteAuthDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        try {
            // Richiedi l'eliminazione delle Info Salvate
            utenteServiceClient.delete(username);

            // Elimina le Credenziali
            utenteAuthDAO.delete(user);
        }
        catch (Exception e)
        { throw new RuntimeException("Eliminazione Fallita: " + e.getMessage(), e); }

    }
}
