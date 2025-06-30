package org.unical.enterprise.utente.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unical.enterprise.utente.data.dao.SeguaceDAO;
import org.unical.enterprise.utente.data.dao.UtenteDAO;
import org.unical.enterprise.utente.data.model.Seguace;
import org.unical.enterprise.utente.data.model.Utente;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeguaceService {

    private SeguaceDAO seguaceDAO;
    private UtenteDAO utenteDAO;

    // Public Methods
    public void seguiUtente(String usernameUtenteSeguace, String usernameUtenteSeguito) {

        // Username inesistenti
        if (usernameUtenteSeguace == null || usernameUtenteSeguito == null ||
            usernameUtenteSeguace.isBlank() || usernameUtenteSeguito.isBlank())
            throw new IllegalArgumentException("Sono necessari usernames definiti");

        // Non si puo' seguire se stessi
        if (usernameUtenteSeguace.equals(usernameUtenteSeguito))
            throw new IllegalArgumentException("Non puoi seguire te stesso");

        // Relazione gia' registrata
        if (seguaceDAO.existsByUtenteSeguace_UsernameAndUtenteSeguito_Username(usernameUtenteSeguace, usernameUtenteSeguito))
            throw new RuntimeException("Utente gia' seguito");


        // Rintraccia gli utenti in questione
        Utente utenteSeguace = utenteDAO.findByUsername(usernameUtenteSeguace)
                .orElseThrow(() -> new RuntimeException("Utente inesistente"));

        Utente utenteSeguito = utenteDAO.findByUsername(usernameUtenteSeguito)
                .orElseThrow(() -> new RuntimeException("Utente inesistente"));

        // Crea e Salva la relazione
        Seguace relazioneSeguaci = Seguace.builder()
                .utenteSeguace(utenteSeguace)
                .utenteSeguito(utenteSeguito)
                .build();

        seguaceDAO.save(relazioneSeguaci);
    }

    public void smettiDiSeguireUtente(String usernameUtenteSeguace, String usernameUtenteSeguito) {
        // Username inesistenti
        if (usernameUtenteSeguace == null || usernameUtenteSeguito == null ||
                usernameUtenteSeguace.isBlank() || usernameUtenteSeguito.isBlank())
            throw new IllegalArgumentException("Sono necessari usernames definiti");

        // Non si puo' seguire se stessi
        if (usernameUtenteSeguace.equals(usernameUtenteSeguito))
            throw new IllegalArgumentException("Non puoi seguire te stesso");

        Optional<Seguace> relazioneSeguace = seguaceDAO.findByUtenteSeguace_UsernameAndUtenteSeguito_Username(usernameUtenteSeguace, usernameUtenteSeguito);

        if (relazioneSeguace.isEmpty()) throw new RuntimeException("Non segui questo utente.");
        else seguaceDAO.delete(relazioneSeguace.get());

    }

    public List<Utente> getAllSeguiti(String usernameUtente) {
        return seguaceDAO.findAllByUtenteSeguace_Username(usernameUtente)
                .stream()
                .map(Seguace::getUtenteSeguito)
                .toList();
    }

    public List<Utente> getAllSeguaci(String usernameUtente) {
        return seguaceDAO.findAllByUtenteSeguito_Username(usernameUtente)
                .stream()
                .map(Seguace::getUtenteSeguace)
                .toList();
    }

}
