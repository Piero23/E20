package org.unical.enterprise.eventoLocation.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.eventoLocation.service.EventoService;
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
    private final EventoService eventoService;

    @GetMapping(value="/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(preferitiService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPreferito(@RequestBody PreferitiDto preferiti) {
        if (eventoService.getByIdNoLocation(preferiti.getEvento_id()) != null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String utenteInserito= preferiti.getUtente_id();
            if (user.equals(utenteInserito)) {
                if (preferitiService.getById(preferiti.getId()) == null) {
                    preferitiService.save(preferiti);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePreferito(@PathVariable("id") Long id) {
        if (preferitiService.getById(id) != null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String utenteInserito=preferitiService.getById(id).getUtente_id();
            if (user.equals(utenteInserito)) {
                preferitiService.delete(id);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/test")
    public String test() {
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