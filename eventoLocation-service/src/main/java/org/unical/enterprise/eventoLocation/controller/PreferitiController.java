package org.unical.enterprise.eventoLocation.controller;

import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.eventoLocation.service.PreferitiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/preferiti")
@AllArgsConstructor
public class PreferitiController {

    private final PreferitiService preferitiService;

    @GetMapping(value="/{id}")
    private ResponseEntity<?> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(preferitiService.getById(id), HttpStatus.OK);
    }

    ///TODO ERRORI CREAZIONE PREFERITI (duplicated keys)
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
}


/* Post API
{
    "utente_id": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "evento_id": 1,
    "status" : false
}
 */