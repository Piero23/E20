package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.EventoDao;
import demacs.unical.esse20.dao.LocationDao;
import demacs.unical.esse20.domain.Evento;
import demacs.unical.esse20.domain.Location;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {

    private final LocationDao locationDao;


    @GetMapping(value="/{id}")
    private Location findById(@PathVariable("id") Long id){
        if (locationDao.findById(id).isPresent())
            return locationDao.findById(id).get();
        return null;
    }

    @PostMapping(value = "/add")
    public boolean addLocation(@RequestBody Location location){
        try {
            locationDao.save(location);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}