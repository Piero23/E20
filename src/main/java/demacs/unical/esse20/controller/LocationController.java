package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import demacs.unical.esse20.data.dto.LocationDto;
import demacs.unical.esse20.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping(value="/{id}")
    private ResponseEntity<Location> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(locationService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return new ResponseEntity<>(locationService.save(location), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable("id") Long id) {
        locationService.delete(id);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Location> replaceLocation(@PathVariable("id") Long id, @RequestBody Location location) {
        return new ResponseEntity<>(locationService.update(location, id), HttpStatus.OK);
    }


}
/*
Esempio POST API
{
      "descrizione": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "chiuso" : true,
    "nome" : "ciao",
    "position" : "aaaaaaaaaa"
}
 */