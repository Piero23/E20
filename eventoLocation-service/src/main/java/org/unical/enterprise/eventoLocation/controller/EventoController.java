package org.unical.enterprise.eventoLocation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.unical.enterprise.shared.dto.BigliettoDto;
import org.unical.enterprise.shared.dto.EventoBasicDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.service.EventoService;

import java.util.List;
import java.util.UUID;


@RequestMapping("/api/evento")
@RestController
@AllArgsConstructor
public class EventoController {

    private final EventoService eventoService;

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

    // TODO non funziona
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventoBasicDto> createEvento(@Valid @RequestBody EventoBasicDto evento) {
        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        String eventoOrganizzatore=evento.getOrganizzatore();
        if (user.equals(eventoOrganizzatore)) {
            return new ResponseEntity<>(eventoService.save(evento), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvento(@PathVariable("id") Long id) {
        if (eventoService.getByIdNoLocation(id) != null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore)) {
                eventoService.delete(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    //TODO vedere se serve
    public ResponseEntity<Evento> replacePerson(@PathVariable("id") Long id, @RequestBody Evento evento) {
        if (eventoService.getByIdNoLocation(id) != null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore)) {
                return new ResponseEntity<>(eventoService.update(evento,id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BigliettoDto>> getBookings(@RequestParam Long id){
        if (eventoService.getByIdNoLocation(id) != null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore)) {
                return ResponseEntity.ok(eventoService.getBigliettiByEvento(id));
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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