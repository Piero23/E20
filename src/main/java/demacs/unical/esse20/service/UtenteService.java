package demacs.unical.esse20.service;

import demacs.unical.esse20.data.dao.UtenteDAO;
import demacs.unical.esse20.data.dto.UtenteDTO;
import demacs.unical.esse20.data.dto.UtenteRegistrationDTO;
import demacs.unical.esse20.data.model.Utente;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtenteService {
    private final UtenteDAO utenteDAO;

    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public List<UtenteDTO> getAllUtenti() {
        return utenteDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // create
    public UtenteDTO registerUtente(UtenteRegistrationDTO dto) {
        if (utenteDAO.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Esiste già un utente con questa email.");
        }

        if (utenteDAO.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Esiste già un utente con questo username.");
        }

        Utente u = Utente.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .dataNascita(dto.getDataNascita())
                .build();

        return toDTO(utenteDAO.save(u));
    }

    // read
    public UtenteDTO getUtenteById(UUID id) {
        return utenteDAO.findById(id)
        .map(this::toDTO)
        .orElseThrow(() -> new RuntimeException("Utente non trovato."));
    }

    public UtenteDTO getUtenteByUsername(String username) {
        return utenteDAO.findByUsername(username)
        .map(this::toDTO)
        .orElseThrow(() -> new RuntimeException("Utente non trovato."));
    }

    // update
    public UtenteDTO updateUtenteById(UUID id, @Valid UtenteDTO utenteDTO) {
        Utente utente = utenteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));

        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setDataNascita(utenteDTO.getData_nascita());

        return toDTO(utenteDAO.save(utente));
    }

    public UtenteDTO updateUtenteByUsername(String username, @Valid UtenteDTO utenteDTO) {
        Utente utente = utenteDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));

        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setDataNascita(utenteDTO.getData_nascita());

        return toDTO(utenteDAO.save(utente));
    }

    // delete
    public void deleteUtenteById(UUID id) {
        Utente utente = utenteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        utenteDAO.delete(utente);
    }

    public void deleteUtenteByUsername(String username) {
        Utente utente = utenteDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        utenteDAO.delete(utente);
    }

    private UtenteDTO toDTO(Utente utente) {
    return UtenteDTO.builder()
        .id(utente.getId())
        .username(utente.getUsername())
        .email(utente.getEmail())
        .data_nascita(utente.getDataNascita())
        .build();
    }

}
