package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dto.UtenteDTO;
import demacs.unical.esse20.data.dto.UtenteRegistrationDTO;
import demacs.unical.esse20.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/utenti/{username}")
    public ResponseEntity<UtenteDTO> getUtenteById(@PathVariable String username) {
        return ResponseEntity.ok(utenteService.getUtenteByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteDTO> register(@Valid @RequestBody UtenteRegistrationDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.registerUtente(utenteDTO));
    }

    @DeleteMapping("/utenti/{username}")
    // @PreAuthorize("hasRole('ADMIN')") o qualcosa del genere
    public ResponseEntity<String> deleteUtenteByUsername(@PathVariable String username) {
        try {
            utenteService.deleteUtenteByUsername(username);
            return ResponseEntity.ok("Utente eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
    }

    @PutMapping("/utenti/{username}")
    public ResponseEntity<UtenteDTO> updateUtente(@PathVariable String username, @Valid @RequestBody UtenteDTO utenteDTO) {
        return ResponseEntity.ok(utenteService.updateUtenteByUsername(username, utenteDTO));
    }
}
