package org.unical.enterprise.utente.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import org.unical.enterprise.utente.client.EventoClient;
import org.unical.enterprise.utente.data.dto.EventoDTO;
import org.unical.enterprise.utente.data.dto.EventoPreferitoRequestDTO;

import org.unical.enterprise.utente.service.SeguaceService;
import org.unical.enterprise.utente.service.UtenteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utente/{username}")
@AllArgsConstructor
public class EventoPreferitoController {

    private EventoClient eventoClient;
    private UtenteService utenteService;
    private SeguaceService seguaceService;

    // Handling della Relazione Seguiti rispetto ad UtenteCorrente
    @GetMapping("/preferiti")
    public ResponseEntity<List<EventoDTO>> getPreferiti(@PathVariable String username,
                                                        JwtAuthenticationToken jwtAuth) {

        String viewerUsername = jwtAuth.getToken().getClaimAsString("username");
        if (viewerUsername == null || viewerUsername.isBlank())
            throw new RuntimeException("Username non disponibile");

        if (!viewerUsername.equals(username) && !seguaceService.isSeguaceDi(viewerUsername, username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        UUID utenteId = utenteService.resolveIdFromUsername(username);

        return ResponseEntity.ok(eventoClient.getAllPreferiti(utenteId));
    }

    @PostMapping("/preferiti")
    public ResponseEntity<String> updateSeguiti(@PathVariable String username,
                                                @Valid @RequestBody EventoPreferitoRequestDTO eventoPreferitoRequestDTO) {

        UUID utenteId = utenteService.resolveIdFromUsername(username);

        eventoClient.aggiungiAiPreferiti(utenteId, eventoPreferitoRequestDTO.getEventoId());
        return ResponseEntity.ok("Evento aggiunto ai Preferiti");

    }

    @DeleteMapping("/preferiti")
    public ResponseEntity<String> deleteUtenteFromSeguiti(@PathVariable String username,
                                                          @Valid @RequestBody EventoPreferitoRequestDTO eventoPreferitoRequestDTO) {

        UUID utenteId = utenteService.resolveIdFromUsername(username);

        eventoClient.rimuoviDaiPreferiti(utenteId, eventoPreferitoRequestDTO.getEventoId());
        return ResponseEntity.ok("Evento rimosso ai Preferiti");

    }
}
