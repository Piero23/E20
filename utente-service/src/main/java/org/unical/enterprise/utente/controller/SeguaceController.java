package org.unical.enterprise.utente.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.dto.UtenteDTO;
import org.unical.enterprise.utente.data.dto.SeguaceRequestDTO;
import org.unical.enterprise.utente.service.SeguaceService;

import java.util.List;

@RestController
@RequestMapping("/api/utente/{username}")
@AllArgsConstructor
public class SeguaceController {

    private SeguaceService seguaceService;

    // Handling della Relazione Seguiti rispetto ad UtenteCorrente
    @GetMapping("/seguiti")
    public ResponseEntity<List<UtenteDTO>> getSeguiti(@PathVariable String username) {
        return ResponseEntity.ok(seguaceService.getAllSeguiti(username));
    }

    @PostMapping("/seguiti")
    public ResponseEntity<String> updateSeguiti(@PathVariable String username,
                                                @Valid @RequestBody SeguaceRequestDTO seguaceRequestDTO) {

        seguaceService.seguiUtente(username, seguaceRequestDTO.getUsername());
        return ResponseEntity.ok(String.format("Utente %s seguito con successo", seguaceRequestDTO.getUsername()));

    }

    @DeleteMapping("/seguiti")
    public ResponseEntity<String> deleteUtenteFromSeguiti(@PathVariable String username,
                                                          @Valid @RequestBody SeguaceRequestDTO seguaceRequestDTO) {

        seguaceService.smettiDiSeguireUtente(username, seguaceRequestDTO.getUsername());
        return ResponseEntity.ok(String.format("Utente %s eliminato dai seguiti con successo", seguaceRequestDTO.getUsername()));

    }

    // Handling della Relazione Seguaci rispetto ad UtenteCorrente
    @GetMapping("/seguaci")
    public ResponseEntity<List<UtenteDTO>> getSeguaci(@PathVariable String username) {
        return ResponseEntity.ok(seguaceService.getAllSeguaci(username));
    }

}
