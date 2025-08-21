package org.unical.enterprise.eventoLocation.controller;

import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.eventoLocation.service.PreferitiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unical.enterprise.shared.dto.EventoBasicDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/preferiti")
@AllArgsConstructor
public class PreferitiController {

    private final PreferitiService preferitiService;

    @GetMapping(value="/{id}")
    private ResponseEntity<?> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(preferitiService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPreferito(@RequestBody PreferitiDto preferiti) {
        preferitiService.save(preferiti);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePreferito(@PathVariable("id") Long id) {
        preferitiService.delete(id);
    }


    @GetMapping("/test")
    private String test() {
        return "Sono PreferitiController";
    }


    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<EventoBasicDto>> getAllPreferiti(@PathVariable String utenteId) {
        return ResponseEntity.ok(preferitiService.getAllEventiByUtenteId(utenteId));
    }

    @PostMapping("/utente/{utenteId}/evento/{eventoId}")
    public ResponseEntity<?> aggiungiAiPreferiti(@RequestHeader(value = "X-Internal-Request", required = false) String internal,
                                                 @PathVariable UUID utenteId,
                                                 @PathVariable Long eventoId) {

        // Solo le Chiamate interne sono Accettate -> tramite Feign
        if (!"true".equals(internal)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        preferitiService.save(PreferitiDto.fromIds(String.valueOf(utenteId), eventoId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/utente/{utenteId}/evento/{eventoId}")
    public ResponseEntity<?> rimuoviDaiPreferiti(@RequestHeader(value = "X-Internal-Request", required = false) String internal,
                                                 @PathVariable UUID utenteId,
                                                 @PathVariable Long eventoId) {

        // Solo le Chiamate interne sono Accettate -> tramite Feign
        if (!"true".equals(internal)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        preferitiService.findAndDelete(PreferitiDto.fromIds(String.valueOf(utenteId), eventoId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

/* Post API
{
    "utente_id": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "evento_id": 1,
    "status" : false
}
 */