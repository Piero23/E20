package org.unical.enterprise.eventoLocation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.unical.enterprise.eventoLocation.data.dto.EventoDto;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.BigliettoDto;
import org.unical.enterprise.shared.dto.EventoBasicDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.service.EventoService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@RequestMapping("/api/evento")
@RestController
@AllArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final UtenteServiceClient utenteServiceClient;

    @GetMapping
    public Page<EventoBasicDto> findAllPagable(Pageable pageable) {
        return eventoService.getPagable(pageable);
    }

    @GetMapping("/search/{string}")
    public Page<EventoBasicDto> search(Pageable pageable, @PathVariable String string) {
        return eventoService.searchPagable(pageable, string);
    }

    // TODO: Guarda qua Carlo
    @GetMapping(value="/{id}")
    private EventoDto findById(@PathVariable("id") Long id){
        return eventoService.getByIdWithLocation(id);
    }

    @PutMapping(path="/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addImage(@PathVariable("id") Long id,
                                           @RequestPart(value = "immagine", required = false) MultipartFile immagine,
                                           Authentication auth) {

        if (eventoService.getByIdNoLocation(id) != null) {
            String user = String.valueOf(Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId());
            UUID eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore.toString())) {
                eventoService.setImage(immagine,id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        byte[] imageBytes = eventoService.getImage(id); // recupera i byte dal DB

        // Imposta il tipo MIME corretto, es. PNG
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventoBasicDto> createEvento(@Valid @RequestBody EventoBasicDto evento, Authentication auth) {

        if(evento.isAge_restricted() && !evento.isB_nominativo())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String user = String.valueOf(Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId());
        UUID eventoOrganizzatore=evento.getOrganizzatore();
        if (user.equals(eventoOrganizzatore.toString())) {
            return new ResponseEntity<>(eventoService.save(evento), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvento(@PathVariable("id") Long id, Authentication auth) {
        if (eventoService.getByIdNoLocation(id) != null) {
            String user = String.valueOf(Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId());
            UUID eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore.toString())) {
                eventoService.delete(id);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(path="/{id}/spots")
    @ResponseStatus(HttpStatus.OK)
    public Long getRemainingSpots(@PathVariable("id") Long id) {

        if (eventoService.getByIdNoLocation(id) != null) {
            return  eventoService.getByIdNoLocation(id).getPosti()-eventoService.getBigliettiByEvento(id).size();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // TODO: Non poter modificare prezzo, organizzatore, riutilizzabile, nominativo
    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<EventoBasicDto> replacePerson(@PathVariable("id") Long id, @RequestBody EventoBasicDto evento, Authentication auth) {

        if(evento.isAge_restricted() && !evento.isB_nominativo())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        if (eventoService.getByIdNoLocation(id) != null) {
            String user = String.valueOf(Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId());
            UUID eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore.toString())) {
                return new ResponseEntity<>(eventoService.update(evento,id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BigliettoDto>> getBookings(@RequestParam Long id, Authentication auth){
        if (eventoService.getByIdNoLocation(id) != null) {
            String user = String.valueOf(Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId());
            UUID eventoOrganizzatore=eventoService.getByIdNoLocation(id).getOrganizzatore();
            if (user.equals(eventoOrganizzatore.toString())) {
                return ResponseEntity.ok(eventoService.getBigliettiByEvento(id));
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/myEvents")
    public ResponseEntity<List<EventoDto>> getMyEvents(Authentication auth){
        UUID user = Objects.requireNonNull(utenteServiceClient.getUtenteByUsername(auth.getName()).getBody()).getId();
            return ResponseEntity.ok(eventoService.getEventiByManager(user));

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