package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.dto.EventoBasicDto;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import demacs.unical.esse20.data.dto.EventoDto;
import demacs.unical.esse20.service.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RequestMapping("/evento")
@RestController
@AllArgsConstructor
public class EventoController /*extends BaseController<Evento,Long, EventoDto>*/ {

    private final EventoDao eventoDao;

    private final LocationDao locationDao;

    private final EventoService eventoService;


    @GetMapping(value="/{id}")
    private ResponseEntity<EventoBasicDto> findById(@PathVariable("id") Long id){
        /*
        if(eventoDao.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new EventoDto(eventoDao.findById(id).get()));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

         */

        return new ResponseEntity<>(eventoService.getByIdNoLocation(id), HttpStatus.OK);
    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventoBasicDto> createEvento(@RequestBody EventoBasicDto evento) {
        return new ResponseEntity<>(eventoService.save(evento), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") Long id) {
        eventoService.delete(id);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Evento> replacePerson(@PathVariable("id") Long id, @RequestBody Evento evento) {
        return new ResponseEntity<>(eventoService.update(evento,id), HttpStatus.OK);
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