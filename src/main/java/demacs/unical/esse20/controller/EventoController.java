package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import demacs.unical.esse20.data.dto.EventoDto;
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

    @GetMapping(value="/{id}")
    private ResponseEntity<EventoDto> findById(@PathVariable("id") Long id){
        if(eventoDao.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new EventoDto(eventoDao.findById(id).get()));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Evento> createEvento(@RequestBody Evento evento) {

        Long locId = evento.getLocation().getId();



        Location location = locationDao.findById(locId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trovata")
                );


        evento.setLocation(location);


        return new ResponseEntity<>(eventoDao.save(evento), HttpStatus.CREATED);
    }


    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") Long id) {
            eventoDao.deleteById(id);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Evento> replacePerson(@PathVariable("id") Long id, @RequestBody Evento evento) {

         eventoDao.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento non trovato")
        );
        evento.setId(id);

        return new ResponseEntity<>(eventoDao.save(evento), HttpStatus.OK);
    }


}
/*
{
    "descrizione": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "organizzatore": "123456789012345678901234567891123456",
    "location": {
        "id": 1
    },
    "nome": "ciaa",
    "posti": 50,
    "b_riutilizzabile": false,
    "b_nominativo": false
}
 */