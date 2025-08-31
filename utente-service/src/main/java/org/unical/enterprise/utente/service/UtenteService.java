package org.unical.enterprise.utente.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.shared.clients.EventoServiceClient;
import org.unical.enterprise.utente.data.dao.UtenteDAO;
import org.unical.enterprise.shared.dto.UtenteDTO;
import org.unical.enterprise.utente.data.dto.UtenteRegistrationDTO;
import org.unical.enterprise.utente.data.model.Utente;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final EventoServiceClient eventoServiceClient;
    private final SeguaceService seguaceService;


    public List<UtenteDTO> getAllUtenti() {
        return utenteDAO.findAll().stream()
                .map(Utente::toSharedDTO)
                .collect(Collectors.toList());
    }

    // create
    @Transactional
    public UtenteDTO registerUtente(UtenteDTO dto) {
        if (utenteDAO.findByEmail(dto.getEmail()).isPresent()) {
            // Due utenti non possono usare la stessa mail
            throw new RuntimeException("Esiste già un utente con questa email.");
        }

        Utente u = Utente.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .dataNascita(dto.getDataNascita())
                .build();

        try {
            return Utente.toSharedDTO(utenteDAO.save(u));
        }
        catch (Exception e)
        { throw new RuntimeException("Errore durante la registrazione lato Utente-Service", e); }
    }

    // read
    public UtenteDTO getUtenteById(UUID id) {
        return utenteDAO.findById(id)
        .map(Utente::toSharedDTO)
        .orElseThrow(() -> new RuntimeException("Utente non trovato."));
    }

    public UtenteDTO getUtenteByUsername(String username) {
        return utenteDAO.findByUsername(username)
        .map(Utente::toSharedDTO)
        .orElseThrow(() -> new RuntimeException("Utente non trovato."));
    }

    // update
    public UtenteDTO updateUtenteById(UUID id, @Valid UtenteDTO utenteDTO) {
        Utente utente = utenteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));

        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setDataNascita(utenteDTO.getDataNascita());

        return Utente.toSharedDTO(utenteDAO.save(utente));
    }

    public UtenteDTO updateUtenteByUsername(String username, @Valid UtenteDTO utenteDTO) {
        Utente utente = utenteDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));

        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setDataNascita(utenteDTO.getDataNascita());

        return Utente.toSharedDTO(utenteDAO.save(utente));
    }

    // delete
    public void deleteUtenteById(UUID id) {
        Utente utente = utenteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        utenteDAO.delete(utente);
    }

    @Transactional
    public void handleEliminazioneUtente(String username) {
        // Cerca l'Utente tramite lo Username
        UtenteDTO utenteDaElinimare = getUtenteByUsername(username);

        try {
            // Elimina Info dell'Utente
            eventoServiceClient.deleteListaPreferiti(utenteDaElinimare.getId());
            seguaceService.deleteUtenteByUsername(username);

            // Eliminazione Utente
            utenteDAO.deleteByUsername(username);
        }
        catch (Exception e)
        { throw new RuntimeException("Elinimazione Fallita: " + e.getMessage(), e); }

    }

    public UUID resolveIdFromUsername(String username) {
        Utente utente = utenteDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente inesistente"));

        return utente.getId();
    }


}
