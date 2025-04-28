package demacs.unical.esse20.service;

import demacs.unical.esse20.ContentNotFoundException;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.dto.EventoBasicDto;
import demacs.unical.esse20.data.dto.LocationDto;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class LocationService {

    LocationDao locationDao;

    public Location getById(Long id){
        return locationDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("location with id " + id + " not found")
        );
    }

    public Location save(Location location){
        return locationDao.save(location);
    }

    public Location update(Location location , Long id){

        locationDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("location with id " + id + " not found")
        );

        location.setId(id);

        return locationDao.save(location);
    }

    public void delete(Long id){

        if(locationDao.findById(id).isEmpty())
            throw new ContentNotFoundException("location with id " + id + " not found");

        locationDao.deleteById(id);
    }

    public Page<LocationDto> getPagable(Pageable pageable){
        Page<Location> locatinos = locationDao.findAll(pageable);

        Page<LocationDto> basic = locatinos.map(location -> new LocationDto(location));

        return basic;
    }
}
