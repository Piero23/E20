package org.unical.enterprise.utente.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.dto.UtenteDTO;
import org.unical.enterprise.utente.service.UtenteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utente")
@AllArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;

    // TODO: Togli Admin
    @GetMapping
    public List<UtenteDTO> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/me")
    public ResponseEntity<UtenteDTO> me(Authentication auth) {
        String username = auth.getName();
        auth.getAuthorities().forEach(System.out::println);
        return ResponseEntity.ok(utenteService.getUtenteByUsername(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UtenteDTO> updateUtente(@Valid @RequestBody UtenteDTO utenteDTO, Authentication auth) {
        if (auth == null || !auth.getName().equals(utenteDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(utenteService.updateUtenteByUsername(auth.getName(), utenteDTO));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UtenteDTO> getUtenteByUsername(@PathVariable String username) {
        return ResponseEntity.ok(utenteService.getUtenteByUsername(username));
    }

    //////// franci l'ho fatto io <3
    @GetMapping("/id/{utenteId}")
    UtenteDTO getById(@PathVariable UUID utenteId) {
        return utenteService.getUtenteById(utenteId);
    }


    // Internal
    @PostMapping("/register")
    ResponseEntity<UtenteDTO> register(@RequestHeader(value = "X-Internal-Request", required = false) String internal,
                                       @Valid @RequestBody UtenteDTO utenteDTO) {

        // Solo le Chiamate interne sono Accettate -> tramite Feign
        if (!"true".equals(internal)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        // Adesso posso salvare in sicurezza le nuove Info dell'Utente
        UtenteDTO newUser = utenteService.registerUtente(utenteDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @DeleteMapping("/{username}")
    // @PreAuthorize("hasRole('ADMIN')") o qualcosa del genere
    public ResponseEntity<String> deleteUtenteByUsername(@RequestHeader(value = "X-Internal-Request", required = false) String internal,
                                                         @PathVariable String username) {

        // Solo le Chiamate interne sono Accettate -> tramite Feign
        if (!"true".equals(internal)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        utenteService.handleEliminazioneUtente(username);

        return ResponseEntity.ok("Utente eliminato con successo");

    }


}
