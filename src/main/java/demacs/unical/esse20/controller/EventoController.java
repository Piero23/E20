package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.EventoDao;
import demacs.unical.esse20.domain.Evento;
import demacs.unical.esse20.dto.EventoDto;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/evento")
@AllArgsConstructor
public class EventoController {

    private final EventoDao eventoDao;


    @GetMapping(value="/{id}")
    private ResponseEntity<EventoDto> findById(@PathVariable("id") Long id){

        if(eventoDao.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new EventoDto(eventoDao.findById(id).get()));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento createPerson(@RequestBody Evento person) { return eventoDao.save(person); }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") Long id) {
        try {
            eventoDao.deleteById(id);
        } catch (EmptyResultDataAccessException ignored) {}
    }

}
