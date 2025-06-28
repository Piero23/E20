package org.unical.enterprise.utente.controller;

import org.unical.enterprise.utente.data.dto.UtenteDTO;
import org.unical.enterprise.utente.data.dto.UtenteRegistrationDTO;
import org.unical.enterprise.utente.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utente")
public class UtenteController {
    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public List<UtenteDTO> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UtenteDTO> getUtenteByUsername(@PathVariable String username) {
        return ResponseEntity.ok(utenteService.getUtenteByUsername(username));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UtenteDTO> getUtenteById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(utenteService.getUtenteById(uuid));
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteDTO> register(@Valid @RequestBody UtenteRegistrationDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.registerUtente(utenteDTO));
    }

    @DeleteMapping("/{username}")
    // @PreAuthorize("hasRole('ADMIN')") o qualcosa del genere
    public ResponseEntity<String> deleteUtenteByUsername(@PathVariable String username) {
        try {
            utenteService.deleteUtenteByUsername(username);
            return ResponseEntity.ok("Utente eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UtenteDTO> updateUtente(@PathVariable String username, @Valid @RequestBody UtenteDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.updateUtenteByUsername(username, utenteDTO));
    }


    @GetMapping("/test")
    private String test() {
        return "Sono UtenteController";
    }
}
