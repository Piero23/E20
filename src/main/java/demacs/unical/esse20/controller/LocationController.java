package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import demacs.unical.esse20.data.dto.LocationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {

    private final LocationDao locationDao;


    @GetMapping(value="/{id}")
    private ResponseEntity<LocationDto> findById(@PathVariable("id") Long id){
        if(locationDao.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new LocationDto(locationDao.findById(id).get()));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Location> createEvento(@RequestBody Location location) {
        return new ResponseEntity<>(locationDao.save(location), HttpStatus.CREATED);
    }


    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") Long id) {
        locationDao.deleteById(id);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Location> replacePerson(@PathVariable("id") Long id, @RequestBody Location location) {

        locationDao.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trovata")
        );
        location.setId(id);

        return new ResponseEntity<>(locationDao.save(location), HttpStatus.OK);
    }


}
/*
Esempio POST API
{
    "descrizione": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "organizzatore": "123456789012345678901234567891123456",
    "location": {
        "id": 1
    },
    "nome": "ciaa",
    "posti": 50,
    "data" : "2019-02-03",
    "b_riutilizzabile": false,
    "b_nominativo": false
}
 */