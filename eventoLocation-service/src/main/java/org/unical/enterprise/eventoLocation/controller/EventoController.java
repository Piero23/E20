package org.unical.enterprise.eventoLocation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.dto.EventoBasicDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.service.EventoService;


@RequestMapping("/api/evento")
@RestController
@AllArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    //TODO Valida e riscrivi tutto

    @GetMapping
    public Page<EventoBasicDto> findAllPagable(Pageable pageable) {
        return eventoService.getPagable(pageable);
    }

    //Non mi ricordo cosa ho fatto co sta roba
    @GetMapping(value="/{id}")
    private EventoBasicDto findById(@PathVariable("id") Long id /* @RequestParam Map<String, String> allParams*/){

        /*
            if (!allParams.isEmpty() && allParams.containsKey("locationFormat") && allParams.size() == 1)
                if (allParams.get("locationFormat").equals("true"))
                    return new ResponseEntity<>(eventoService.getByIdWithLocation(id), HttpStatus.OK);

            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         */

        return eventoService.getByIdNoLocation(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    //Validate
    public ResponseEntity<EventoBasicDto> createEvento(@RequestBody EventoBasicDto evento) {
        return new ResponseEntity<>(eventoService.save(evento), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvento(@PathVariable("id") Long id) {
        eventoService.delete(id);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Evento> replacePerson(@PathVariable("id") Long id, @RequestBody Evento evento) {
        return new ResponseEntity<>(eventoService.update(evento,id), HttpStatus.OK);
    }

    @GetMapping("/test")
    private String test() {
        return "Sono EventoController";
    }

}

/*
{
    "descrizione": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "organizzatore": "123456789012345678901234567891123456",
    "locationId":  3,
    "nome": "ciaa",
    "posti": 50,
    "b_riutilizzabile": false,
    "b_nominativo": false,
    "data" : "2040-10-10"
}
 */