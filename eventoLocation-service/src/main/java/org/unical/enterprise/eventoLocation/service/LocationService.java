package org.unical.enterprise.eventoLocation.service;

import org.unical.enterprise.eventoLocation.ContentNotFoundException;
import org.unical.enterprise.eventoLocation.data.dao.LocationDao;
import org.unical.enterprise.eventoLocation.data.dto.LocationDto;
import org.unical.enterprise.eventoLocation.data.entities.Location;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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
