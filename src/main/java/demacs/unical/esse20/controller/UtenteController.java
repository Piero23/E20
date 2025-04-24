package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dto.UtenteDTO;
import demacs.unical.esse20.data.dto.UtenteRegistrationDTO;
import demacs.unical.esse20.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UtenteController {
    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public List<UtenteDTO> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/utenti/{id}")
    public ResponseEntity<UtenteDTO> getUtenteById(@PathVariable UUID id) {
        return ResponseEntity.ok(utenteService.getUtenteById(id));
    }

    @PostMapping
    public ResponseEntity<UtenteDTO> createUtente(@Valid @RequestBody UtenteDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.createUtente(utenteDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteDTO> register(@Valid @RequestBody UtenteRegistrationDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.registerUtente(utenteDTO));
    }

    @DeleteMapping("/utenti/{id}")
    // @PreAuthorize("hasRole('ADMIN')") o qualcosa del genere
    public ResponseEntity<String> deleteUtenteById(@PathVariable UUID id) {
        try {
            utenteService.deleteUtenteById(id);
            return ResponseEntity.ok("Utente eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
    }

    @PutMapping("/utenti/{id}")
    public ResponseEntity<UtenteDTO> updateUtente(@PathVariable UUID id, @Valid @RequestBody UtenteDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.updateUtenteById(id, utenteDTO));
    }
}
