package org.unical.enterprise.eventoLocation.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.eventoLocation.data.dto.LocationDto;
import org.unical.enterprise.eventoLocation.data.entities.Location;
import org.unical.enterprise.eventoLocation.service.LocationService;

@RestController
@RequestMapping("/api/location")
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;


    @GetMapping
    public Page<LocationDto> findAllPagable(Pageable pageable) {
        return locationService.getPagable(pageable);
    }



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


    @GetMapping("/test")
    private String test() {
        return "Sono LocationController";
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